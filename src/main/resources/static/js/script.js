const token = localStorage.getItem("token");

console.log("Token:", token);

if(!token){
   window.location.href="login.html";
}

document.addEventListener("DOMContentLoaded", async ()=>{
    try{
        const res = await fetch("http://localhost:8080/api/user",{
            headers:{
                "Authorization":"Bearer "+token
            }
        });

        console.log("User fetch response:", res);

        if(!res.ok) throw new Error("Auth failed");

        const user = await res.json();

        /* SAVE LATEST USER */
        localStorage.setItem("user", JSON.stringify(user));

        /* PROFILE LETTER */
        if(user.name){
            document.querySelector(".profile").innerText =
                user.name.charAt(0).toUpperCase();
        }

        /* WALLET */
        document.getElementById("walletAmount").innerText =
            safeMoney(user.wallet).toLocaleString();

    }catch(err){
        console.log("User fetch failed:",err);
    }

});


setInterval(()=>{
    console.log("Market data refreshing...");
}, 4000);


// Card click demo interaction
document.querySelectorAll(".stock-card").forEach(card=>{
    card.addEventListener("click", ()=>{
        alert("Open Stock Details Page");
    });
});

function goToStockPage(){
   window.location.href = "stock.html";
}

function toggleProfile(){
    document.getElementById("profilePanel")
    .classList.toggle("show");
}

// Click stock → open stock page with data
document.querySelectorAll(".stock-box").forEach(card=>{
    card.addEventListener("click", ()=>{

        const name = card.dataset.name;
        const price = card.dataset.price;

        localStorage.setItem("selectedStock",
            JSON.stringify({ name, price })
        );

        window.location.href="stock.html";
    });
});

function logout() {
    // 🧹 Remove token
    localStorage.removeItem("token");
    // 🔄 Redirect
    window.location.href = "/login.html";
  }


function goToWallet(){
    window.location.href = "wallet.html";
 }
