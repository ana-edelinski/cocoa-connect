<template>
  <div class="container">
    <div v-if="factory" class="factory-info">
      <div class="factory-details-box">
        <div class="factory-details">
          <p v-if="factory.workingHours">
            <strong>⏰ Working Hours:</strong>
            {{ formatWorkingHours(factory.workingHours) }}
          </p>
          <p><strong>❔ Status:</strong> {{ factory.factoryStatus }}</p>
          <p><strong>📍 Location:</strong> {{ factory.city }}, {{ factory.country }}</p>
          <p v-if="factory.averageRating"><strong>⭐ Rating:</strong> {{ factory.averageRating }}</p>
          <div v-if="factory.comments && factory.comments.length">
            <strong>Comments:</strong>
            <ul>
              <li v-for="comment in factory.comments" :key="comment.id">{{ comment.text }}</li>
            </ul>
          </div>
        </div>
        <div class="factory-logo">
          <img v-if="factory.logo" :src="factory.logo" alt="Factory Logo" />
        </div>
      </div>
    </div>

    <section class="chocolates-section">
      <h2 class="section-title">Our products</h2>
      <div class="chocolates-grid">
        <div v-for="chocolate in chocolates" :key="chocolate.id" class="chocolate-card">
          <div class="price-ribbon">{{ chocolate.price }} RSD</div>
          <img :src="chocolate.image" alt="Chocolate Image" class="chocolate-image" />
          <h3>{{ chocolate.name }}</h3>
          <p class="chocolate-description">{{ chocolate.description }}</p>
          <p class="chocolate-detail"><strong>Type:</strong> {{ chocolate.type }}</p>
          <p class="chocolate-detail"><strong>Kind:</strong> {{ chocolate.kind }}</p>
          <p class="chocolate-detail"><strong>Weight:</strong> {{ chocolate.weight }} g</p>
          <div class="quantity-row">
            <p class="chocolate-detail" v-if="loggedUser && loggedUser.role !== 'EMPLOYEE'">
              <strong>Quantity:</strong> {{ chocolate.quantity }}
            </p>
            <template v-if="loggedUser && loggedUser.role === 'EMPLOYEE' && loggedUser.factoryWorkingId === factory.id">
              <div class="quantity-input">
                <p class="chocolate-detail"><strong>Quantity:</strong></p>
                <input v-model="chocolate.newQuantity" type="number" />
                <button class="btn btn-done" @click="updateQuantity(chocolate.id)">Done</button>
              </div>
            </template>
          </div>
          <div class="buttons" v-if="loggedUser && (loggedUser.role === 'MANAGER' || loggedUser.role === 'EMPLOYEE')">
            <button class="btn btn-delete" 
                    v-if="loggedUser.role === 'MANAGER' && isManagerOfFactory(chocolate.factoryId)"
                    @click="deleteChocolate(chocolate.id)">
              Delete
            </button>
            <button class="btn btn-edit" 
                    v-if="loggedUser.role === 'MANAGER' && isManagerOfFactory(chocolate.factoryId)"
                    @click="editChocolate(chocolate.id)">
              Edit
            </button>
          </div>
          <div v-if="loggedUser && loggedUser.role === 'CUSTOMER' && isFactoryOpen()" class="purchase-section">
            <p><strong>Desired Quantity:</strong></p>
            <div class="quantity-controls">
                <button class="btn btn-quantity" @click="decreaseDesiredQuantity(chocolate)">-</button>
                <input v-model.number="chocolate.purchaseQuantity" type="number" @change="validateQuantity(chocolate)" />
                <button class="btn btn-quantity" @click="increaseDesiredQuantity(chocolate)">+</button>
            </div>
            <button class="btn btn-done" @click="confirmPurchase(chocolate)" :disabled="chocolate.quantity === 0">Add to Cart</button>
        </div>
        <p v-if="loggedUser && loggedUser.role === 'CUSTOMER' && !isFactoryOpen()" style="color: red;">
            Factory is closed. Orders cannot be placed at this time.
        </p>
        </div>
      </div>
    </section>

    <section class="chocolates-section">
  <div class="comments-grid">
    <div class="tab-content" v-if="loggedUser.role === 'MANAGER' || loggedUser.role === 'ADMINISTRATOR'">
      <div class="comment-list">
        <div class="comment-header">Comments:</div>
        <div class="comment" v-for="comment in managersComments" :key="comment.id">
          <div class="comment-details">
            <div class="comment-author" v-if="comment.user && comment.user.name">
              <strong>{{ comment.user.name }} {{ comment.user.surname }}</strong>
            </div>
            <div class="comment-text">{{ comment.grade }} ⭐ {{ comment.text }}</div>
            <div class="comment-status"><strong>Status:</strong> {{ comment.status }}</div>
          </div>
          <div class="button-column" v-if="comment.status === 'PROCESSING'">
            <button @click="aproveComment(comment.id)" class="btn-approve">Approve</button>
            <button @click="declineComment(comment.id)" class="btn-reject">Reject</button>
          </div>
        </div>
      </div>
    </div>

    <div class="tab-content" v-if="loggedUser.role === 'CUSTOMER'">
      <div class="comment-list">
        <div class="comment-header">Comments:</div>
        <div class="comment" v-for="comment in usersComments" :key="comment.id">
          <div class="comment-details">
            <div class="comment-author" v-if="comment.user && comment.user.name">
              <strong>{{ comment.user.name }} {{ comment.user.surname }}</strong>
            </div>
            <div class="comment-text">{{ comment.grade }} ⭐ {{ comment.text }}</div>
            <div class="comment-status"><strong>Status:</strong> {{ comment.status }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>

  </div>
</template>



<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();
const route = useRoute();
const factoryId = route.params.factoryId;

const loggedUser = ref({});

const factory = ref({});
const chocolates = ref([]);
const managersComments = ref([]);
const usersComments = ref([]);


onMounted(() => {
  const storedUser = localStorage.getItem('loggedUser');
  if (storedUser) {
    loggedUser.value = JSON.parse(storedUser);
    console.log("Logged user:", loggedUser.value);
  }
  loadFactory();
  loadChocolates();
  loadManagersComments();
  loadUsersComments();
});

function loadFactory() {
  axios.get('http://localhost:8080/chocolate-factory/rest/factories/' + factoryId)
    .then(response => {
      factory.value = response.data;
    })
    .catch(error => console.error(error));
}


function loadManagersComments() {
  axios.get('http://localhost:8080/chocolate-factory/rest/comments/getAllForFactory/' + factoryId)
    .then(response => {
      managersComments.value = response.data;
    })
    .catch(error => console.error(error));
}
function loadUsersComments() {
  axios.get('http://localhost:8080/chocolate-factory/rest/comments/getAllApprovedForFactory/' + factoryId)
    .then(response => {
      usersComments.value = response.data;
    })
    .catch(error => console.error(error));
}

function loadChocolates() {
  axios.get('http://localhost:8080/chocolate-factory/rest/chocolates/getAllForFactory/' + factoryId)
    .then(response => {
      chocolates.value = response.data
        .filter(chocolate => !chocolate.deleted)
        .map(chocolate => {
          chocolate.purchaseQuantity = 1;
          chocolate.newQuantity = chocolate.quantity; 
          return chocolate;
        });
    })
    .catch(error => console.error(error));
}
function deleteChocolate(chocolateId) {
  axios.delete(`http://localhost:8080/chocolate-factory/rest/chocolates/${chocolateId}`)
    .then(() => {
      chocolates.value = chocolates.value.filter(chocolate => chocolate.id !== chocolateId);
    })
    .catch(error => console.error(error));
}

function editChocolate(chocolateId) {
  router.push({ name: 'editChocolate', params: { chocolateId } });
}

function updateQuantity(chocolateId) {
  const chocolate = chocolates.value.find(choc => choc.id === chocolateId);
  chocolate.quantity = chocolate.newQuantity;
  if (chocolate) {
    axios.put(`http://localhost:8080/chocolate-factory/rest/chocolates/${chocolateId}`, chocolate)
      .then(response => {
        chocolate.quantity = chocolate.newQuantity;
        chocolate.editQuantity = false;
      })
      .catch(error => console.error(error));
     
  }
}
function increaseDesiredQuantity(chocolate) {
  if (chocolate.purchaseQuantity < chocolate.quantity) {
    chocolate.purchaseQuantity++;
  }
}

function decreaseDesiredQuantity(chocolate) {
  if (chocolate.purchaseQuantity > 1) {
    chocolate.purchaseQuantity--;
  }
}

function confirmPurchase(chocolate) {
  console.log("Confirming purchase for chocolate:", chocolate);

    if (chocolate.quantity === 0) {
        alert('This product is out of stock and cannot be ordered.');
        return;
    }

    const cartItem = {'chocolate': chocolate, 'quantity': chocolate.purchaseQuantity};

    let cartStr = localStorage.getItem('cart');
    let cart = JSON.parse(cartStr);

    if (cart) {
        if (cart.factoryId !== factoryId) {
            alert('Cannot order from different factories');
            return;
        }

        const existingItemIndex = cart.items.findIndex(item => item.chocolate.id === chocolate.id);
        if (existingItemIndex === -1) {
            cart.items.push(cartItem);
        } else {
            cart.items[existingItemIndex].quantity += chocolate.purchaseQuantity;
        }
    } else {
        cart = { 'items': [cartItem], 'factoryId' : factoryId};
    }

    const cartForSave = JSON.stringify(cart);
    localStorage.setItem('cart', cartForSave);
    alert('✔Added to cart.');
}





function validateQuantity(chocolate) {
  if (chocolate.purchaseQuantity < 0) {
    chocolate.purchaseQuantity = 0;
  } else if (chocolate.purchaseQuantity > chocolate.quantity) {
    chocolate.purchaseQuantity = chocolate.quantity;
  }
}

function formatWorkingHours(workingHours) {
  return `${workingHours.startTime.slice(0, 5)} - ${workingHours.endTime.slice(0, 5)}`;
}

function isManagerOfFactory(factoryId) {
  return factory.value && factory.value.managerId === loggedUser.value.id;
}

function isFactoryEmployee(factoryId) {
  console.log("Checking factory employee:", factory.value, loggedUser.value);
  return factory.value && factory.value.id === loggedUser.value.factoryWorkingId;
}



function aproveComment(commentId) {
  axios.put('http://localhost:8080/chocolate-factory/rest/comments/aprove/' + commentId)
    .then(response => {
      loadManagersComments();
      alert("Comment is aproved");
    })
    .catch(error => alert("Comment is not aproved"));
}

function declineComment(commentId) {
  axios.put('http://localhost:8080/chocolate-factory/rest/comments/reject/' + commentId)
    .then(response => {
      loadManagersComments();
      alert("Comment is rejected");
    })
    .catch(error => alert("Comment is not rejected"));
}

function isFactoryOpen() {
  return factory.value.factoryStatus === 'OPENED';
}


</script>


<style>
.container {
  width: 90%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  font-family: "Poppins", sans-serif;
}

header {
  text-align: center;
  margin-bottom: 20px;
}

.factory-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 50px;
  margin-left: 170px;
  margin-top: 50px;
}

.factory-details-box {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: center;
  width: 80%;
  padding: 20px;
  text-align: left;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.factory-details {
  flex: 1;
  align-items: left;
}

.factory-logo {
  flex: 0 1 200px;
  margin-left: auto;
}

.factory-logo img {
  max-width: 100%;
  height: auto;
  width: auto;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.chocolates-section {
  margin-top: 20px;
}

.section-title {
  color: #2D1E17;
  text-align: center;
  font-family: "Poppins", sans-serif;
  font-size: xx-large;
  margin-bottom: 15px;
  margin-top: -30px;
}

.chocolates-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
}

.chocolate-card {
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  overflow: hidden;
  width: 350px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 20px;
  position: relative;
  transition: transform 0.2s;
}

.chocolate-card:hover {
  transform: scale(1.05);
}

.price-ribbon {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: #523F31;
  color: #fff;
  padding: 5px 10px;
  font-size: 14px;
  font-weight: bold;
  border-radius: 5px;
  z-index: 1;
}

.chocolate-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  object-position: center;
  border-radius: 5px;
  margin-bottom: 10px;
}

.chocolate-card h3 {
  margin: 10px 0 5px;
}

.chocolate-description {
  margin: 0 0 15px;
  text-align: center;
  font-size: 16px;
}

.chocolate-detail {
  margin: 5px 0;
}

.quantity-row {
  display: flex;
  align-items: center;
  justify-content: center;
}

.quantity-input {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 5px;
  margin-left: 10px;
}

.btn {
  padding: 10px 20px;
  cursor: pointer;
  border: none;
  border-radius: 5px;
  color: #fff;
  background-color: #523F31;
  transition: background-color 0.3s ease;
  font-weight: 500;
  font-family: "Poppins", sans-serif;
}

.btn:hover {
  background-color: #796254;
}

.btn-delete {
  background-color: #523F31;
  margin-bottom: 10px;
  font-family: "Poppins", sans-serif;
}

.btn-edit {
  background-color: #523F31;
  font-family: "Poppins", sans-serif;
  width: fit-content;
  height: fit-content;
}

.buttons {
  display: flex;
  flex-direction: row;
  justify-content: center;
  gap: 10px;
}

.btn-edit-quantity {
  background-color: #523F31;
  font-family: "Poppins", sans-serif;
  margin-top: 10px;
}

.btn-done {
  background-color: #523F31;
  color: #fff;
  border: none;
  padding: 8px 12px;
  margin-top: 15px;
  margin-left: 10px;
  margin-bottom: 10px;
  border-radius: 5px;
  cursor: pointer;
}

.btn-done:hover {
  background-color: #796254;
}

input[type="number"]::-webkit-outer-spin-button,
input[type="number"]::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

input[type="number"] {
  border: 1px solid #ddd;
  border-radius: 5px;
  width: 80px;
  box-sizing: border-box;
}

.edit-quantity-section {
  margin-top: 10px;
  width: 100%;
}

.quantity-controls {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 10px;
}

.quantity-controls input[type="number"] {
  width: 60px;
  text-align: center;
  border: 1px solid #ddd;
  border-radius: 0;
  margin-left: -10px;
  margin-right: -10px;
  height: 27px;
  margin-top:20px;
}

.btn-quantity {
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: white;
  color: #523F31;
  border: 1px solid #ddd;
  border-radius: 0;
  padding: 5px 10px;
  cursor: pointer;
}

.btn-quantity:first-of-type {
  color: #523F31;
  border-radius: 5px 0 0 5px;
  border-right: none;
  height: 27px;
}

.btn-quantity:last-of-type {
  color: #523F31;
  border-radius: 0 5px 5px 0;
  border-left: none;
  height: 27px;
}

.btn-quantity:hover {
  background-color: #796254;
  color: #fff;
}

.comment-list {
  width: 90%;
  margin-left: 45px;
}

.comment {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 10px;
  margin-bottom: 10px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.comment-header {
  display: flex;
  align-items: left;
  margin-bottom: 5px;
}

.comment-rating {
  background-color: #523F31;
  color: #fff;
  padding: 5px 10px;
  font-size: 14px;
  font-weight: bold;
  border-radius: 5px;
}

.comment-text {
  margin-bottom: 10px;
  font-size: 16px;
}

.comment-status {
  font-size: 14px;
  color: #666;
}

.button-column {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.btn-approve {
  background-color: #28a745;
  color: #fff;
  border: none;
  padding: 8px 12px;
  border-radius: 5px;
  cursor: pointer;
}

.btn-approve:hover {
  background-color: #218838;
}

.btn-reject {
  background-color: #dc3545;
  color: #fff;
  border: none;
  padding: 8px 12px;
  border-radius: 5px;
  cursor: pointer;
}

.btn-reject:hover {
  background-color: #c82333;
}

.btn:disabled {
  background-color: #d3d3d3; 
  cursor: not-allowed; 
  color: #888888; 
}
</style>
