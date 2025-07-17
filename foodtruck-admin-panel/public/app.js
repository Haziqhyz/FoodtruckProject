// Firebase v9 Modular SDK
import { initializeApp } from "https://www.gstatic.com/firebasejs/9.6.10/firebase-app.js";
import { getFirestore, collection, addDoc } from "https://www.gstatic.com/firebasejs/9.6.10/firebase-firestore.js";
import { getAuth, signInWithEmailAndPassword, signOut, onAuthStateChanged } from "https://www.gstatic.com/firebasejs/9.6.10/firebase-auth.js";

// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyBT649smCyO9sXFG4WHBm5i_n9yCLBsOlM",
  authDomain: "foodtrucklocationtracker.firebaseapp.com",
  projectId: "foodtrucklocationtracker",
  storageBucket: "foodtrucklocationtracker.firebasestorage.app",
  messagingSenderId: "793531785293",
  appId: "1:793531785293:web:436c5ea6e6d479373efc04",
  measurementId: "G-0QR0XL3FD1"

};

// ✅ Initialize
firebase.initializeApp(firebaseConfig);
const app = initializeApp(firebaseConfig);
const db = getFirestore(app);
const auth = getAuth(app);
const analytics = getAnalytics(app);


// ✅ DOM
const loginDiv = document.getElementById("loginDiv");
const formDiv = document.getElementById("formDiv");
const loginBtn = document.getElementById("loginBtn");
const addBtn = document.getElementById("addBtn");
const logoutBtn = document.getElementById("logoutBtn");

// ✅ Auth
loginBtn.onclick = () => {
  const email = document.getElementById("email").value;
  const pass = document.getElementById("password").value;
  signInWithEmailAndPassword(auth, email, pass)
    .then(() => alert("Login successful!"))
    .catch(err => alert(err.message));
};

logoutBtn.onclick = () => signOut(auth);

// ✅ Add Truck
addBtn.onclick = async () => {
  const Type = document.getElementById("Type").value.trim();
  const desc = document.getElementById("description").value.trim();
  const lat = parseFloat(document.getElementById("latitude").value);
  const lng = parseFloat(document.getElementById("longitude").value);

  if (!Type || !desc || isNaN(lat) || isNaN(lng)) {
    alert("Please fill in all fields correctly.");
    return;
  }

  try {
    await addDoc(collection(db, "food_truck"), {
      type: type,
      locationDescription: desc,
      latitude: lat,
      longitude: lng
    });
    alert("Food Truck Added!");
  } catch (err) {
    console.error(err);
    alert("Error adding truck");
  }
};

// ✅ Auth State Listener
onAuthStateChanged(auth, user => {
  if (user) {
    loginDiv.style.display = "none";
    formDiv.style.display = "block";
  } else {
    loginDiv.style.display = "block";
    formDiv.style.display = "none";
  }
});
