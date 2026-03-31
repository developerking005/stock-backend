const API = "http://localhost:8080/api/auth";

async function signup(){


const name = document.getElementById("name").value;
const phone = document.getElementById("phone").value;
const password = document.getElementById("password").value;


const data = {
name: name,
phone: phone,
password: password
};

const res = await fetch(API+"/signup",{
method:"POST",
headers:{ "Content-Type":"application/json"},
body:JSON.stringify(data)
});

const result = await res.json();

localStorage.setItem("token", result.token);
localStorage.setItem("user", JSON.stringify(result));

if(result.token){
    window.location.href="newindex.html";
}
else{
    alert("Signup failed: " + result.message)};

}


async function login(){

const res = await fetch(API+"/login",{
method:"POST",
headers:{ "Content-Type":"application/json"},
body: JSON.stringify({
    phone: phone,
    password: password
})
});

const result = await res.json();

localStorage.setItem("token", data.token);
localStorage.setItem("user", JSON.stringify({
    name: data.name,
    wallet: data.wallet
}));

if(result.token){
    window.location.href="newindex.html";
}
else{
    alert("Signup failed: " + result.message)};

}

