const stockData = JSON.parse(localStorage.getItem("selectedStock"));

const startPrice = stockData ? Number(stockData.price) : 100;

let prices = [];

let current = startPrice;

async function refreshUser(){

    const token = localStorage.getItem("token");

    const res = await fetch("http://localhost:8080/api/user/",{
        headers:{ "Authorization":"Bearer "+token }
    });

    const user = await res.json();

    localStorage.setItem("user", JSON.stringify(user));

    const walletEl = document.getElementById("walletAmount");
    if(walletEl){
        walletEl.innerText = safeMoney(user.wallet).toFixed(2);
    }

    return user;
}

/* load user instantly */
refreshUser();

// generate past movement history
for (let i = 0; i < 120; i++) {

    const volatility = current * 0.01;

    const trend = Math.sin(i / 15) * volatility;

    current += trend + (Math.random() - 0.5) * volatility;

    if (current < 1) current = 1;

    prices.push(current);
}

if (stockData) {
    document.querySelector(".stock-header h2").innerText = stockData.name;
    document.getElementById("stockPrice").innerText = "₹ " + startPrice;
    document.querySelector(".trade-card h3").innerText = stockData.name;
}

const canvas = document.getElementById("stockChart");
const ctx = canvas.getContext("2d");

canvas.width = 900;
canvas.height = 400;

/* ================= CONFIG ================= */

const CONFIG = {
    maxPoints: 180,
    xGap: 5,
    minPrice: 15,
    updateSpeed: 800
};

/* ================= STATE ================= */

let buyPoints = []; // store index

let mode = "BUY";
let holdingsQty = 0;
let avgBuyPrice = 0;
let balance = 0;

/* ================= DOM ================= */

const priceEl = document.getElementById("stockPrice");
const qtyInput = document.getElementById("qtyInput");
const reqText = document.getElementById("reqText");
const balanceText = document.getElementById("balanceText");
const tradeBtn = document.getElementById("tradeBtn");

/* ================= PRICE ================= */

function generatePrice() {
    const last = prices[prices.length - 1];
    return Math.max(CONFIG.minPrice, last + (Math.random() - 0.5));
}

function getCurrentPrice() {
    return prices[prices.length - 1];
}

/* ================= DRAW GRAPH ================= */
function drawGraph() {

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    const max = Math.max(...prices);
    const min = Math.min(...prices);
    const range = max - min || 1;

    /* GRID */
    ctx.strokeStyle = "#eee";
    ctx.lineWidth = 1;

    for (let i = 0; i < 5; i++) {
        let y = (canvas.height / 5) * i;
        ctx.beginPath();
        ctx.moveTo(0, y);
        ctx.lineTo(canvas.width, y);
        ctx.stroke();
    }

    /* PRICE LABELS */
    ctx.fillStyle = "#888";
    ctx.font = "12px Arial";

    for (let i = 0; i <= 4; i++) {
        const value = min + (range / 4) * i;
        const y = canvas.height - (canvas.height / 4) * i;
        ctx.fillText(value.toFixed(2), canvas.width - 50, y);
    }

    /* LINE */
    ctx.beginPath();
    ctx.strokeStyle = "#00b386";
    ctx.lineWidth = 2;

    prices.forEach((price, i) => {

        const x = i * CONFIG.xGap;
        const normalized = (price - min) / range;
        const y = canvas.height - normalized * canvas.height;

        if (i === 0) {
            ctx.moveTo(x, y);
        } else {
            const prevX = (i - 1) * CONFIG.xGap;
            const prevY = canvas.height - ((prices[i - 1] - min) / range) * canvas.height;

            const cx = (prevX + x) / 2;
            const cy = (prevY + y) / 2;

            ctx.quadraticCurveTo(prevX, prevY, cx, cy);
        }

    });

    ctx.stroke();

    /* BUY MARKERS */
    ctx.strokeStyle = "yellow";

    buyPoints.forEach(index => {
        const x = index * CONFIG.xGap;
        ctx.beginPath();
        ctx.moveTo(x, 0);
        ctx.lineTo(x, canvas.height);
        ctx.stroke();
    });
}

/* ================= LIVE UPDATE ================= */

setInterval(() => {

    prices.push(generatePrice());

    // If overflow, shift prices AND shift buyPoints indexes
    if (prices.length > CONFIG.maxPoints) {
        prices.shift();

        // Decrease all buy indexes by 1
        buyPoints = buyPoints
            .map(i => i - 1)
            .filter(i => i >= 0); // remove if out of chart
    }

    priceEl.innerText = "₹ " + getCurrentPrice().toFixed(2);

    updateApprox();
    drawGraph();

}, CONFIG.updateSpeed);

/* ================= APPROX ================= */

function updateApprox() {

    const qty = Number(qtyInput.value) || 0;
    const price = getCurrentPrice();
    const total = qty * price;

    reqText.innerText = "Approx req: ₹" + total.toFixed(2);

    if(mode === "BUY"){
        balanceText.innerText = "Buy Amount: ₹" + total.toFixed(2);
    }
}

qtyInput.addEventListener("input", updateApprox);

/* ================= TAB SWITCH ================= */

document.querySelectorAll(".tab").forEach(tab => {
    tab.addEventListener("click", () => {

        document.querySelectorAll(".tab")
            .forEach(t => t.classList.remove("active"));

        tab.classList.add("active");
        mode = tab.innerText.trim();

        tradeBtn.innerText = mode === "BUY" ? "Buy" : "Sell";
        tradeBtn.style.background =
            mode === "BUY" ? "#00b386" : "#ff4d4f";
    });
});

/* ================= TRADE ================= */

tradeBtn.addEventListener("click", () => {
    mode === "BUY" ? executeBuy() : executeSell();
});

/* ================= BUY ================= */

function executeBuy(){

    const qty = Number(qtyInput.value);
    if (!qty || qty <= 0) return alert("Enter valid Qty");

    const price = getCurrentPrice();
    const totalCost = qty * price;

    const totalOldValue = holdingsQty * avgBuyPrice;

    holdingsQty += qty;
    avgBuyPrice = (totalOldValue + totalCost) / holdingsQty;

    buyPoints.push(prices.length - 1);

    updateApprox();
}

/* ================= SELL ================= */

async function executeSell(){

    const qty = Number(qtyInput.value);
    if (!qty || qty <= 0) return alert("Enter valid Qty");
    if (qty > holdingsQty) return alert("Not enough shares");

    const sellPrice = getCurrentPrice();

    const profit = (sellPrice - avgBuyPrice) * qty;

    holdingsQty -= qty;

    /* BACKEND WALLET UPDATE */
    try{
        const token = localStorage.getItem("token");

        const res = await fetch("http://localhost:8080/api/user/wallet/update",{
            method:"POST",
            headers:{
                "Content-Type":"application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify({
                amount: profit
            })
        });

        const data = await res.json();

        /* UPDATE LOCAL USER */
        const user = JSON.parse(localStorage.getItem("user"));
        user.wallet = data.wallet;
        localStorage.setItem("user", JSON.stringify(user));

        /* UPDATE UI */
        document.getElementById("walletAmount").innerText =
            data.wallet.toFixed(2);

    }catch(e){
        console.log("Wallet update failed",e);
    }

    updateApprox();
}

/* ================= BALANCE ================= */

function updateBalance() {
    balanceText.innerText =
        "Balance: ₹" + balance.toFixed(2);
}

function safeMoney(val){
    const n = Number(val);
    return isNaN(n) ? 0 : n;
}

/* ================= START ================= */

drawGraph();
updateBalance();

function goToHomePage(){
    window.location.href = "newindex.html";
 }
