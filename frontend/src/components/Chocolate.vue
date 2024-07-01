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
          <p v-if="factory.comment"><strong>Comment:</strong> {{ factory.comment }}</p>
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
          <img :src="chocolate.image" alt="Chocolate Image" class="chocolate-image" />
          <h3>{{ chocolate.name }}</h3>
          <p class="chocolate-price"><strong> {{ chocolate.price }} RSD </strong></p>
          <p class="chocolate-description">{{ chocolate.description }}</p>
          <p class="chocolate-detail"><strong>Type:</strong> {{ chocolate.type }}</p>
          <p class="chocolate-detail"><strong>Kind:</strong> {{ chocolate.kind }}</p>
          <p class="chocolate-detail"><strong>Weight:</strong> {{ chocolate.weight }} g</p>  
          <div class="quantity-row">
          <template v-if="loggedUser && loggedUser.role !== 'EMPLOYEE'">
            <p class="chocolate-detail"><strong>Quantity:</strong> {{ chocolate.quantity }}</p>
          </template>
          <span v-if="loggedUser && loggedUser.role === 'EMPLOYEE'" class="quantity-input">
            <p class="chocolate-detail"><strong>Quantity:</strong></p>
            <input v-model="chocolate.newQuantity" type="number" @blur="updateQuantity(chocolate.id)" />
            <button class="btn btn-done" @click="updateQuantity(chocolate.id)">✔</button>
          </span>
        </div>
          <div class="buttons" v-if="loggedUser && (loggedUser.role === 'MANAGER' || loggedUser.role === 'EMPLOYEE')">
            <button class="btn btn-delete" v-if="loggedUser.role === 'MANAGER'" @click="deleteChocolate(chocolate.id)">Delete</button>
            <button class="btn btn-edit" v-if="loggedUser.role === 'MANAGER'" @click="editChocolate(chocolate.id)">Edit</button>
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

const loggedUser = ref(null);

const factory = ref(null);
const chocolates = ref([]);

onMounted(() => {
  const storedUser = localStorage.getItem('loggedUser');
  if (storedUser) {
    loggedUser.value = JSON.parse(storedUser);
  }
  loadFactory();
  loadChocolates();
});

function loadFactory() {
  axios.get('http://localhost:8080/chocolate-factory/rest/factories/' + factoryId)
    .then(response => {
      factory.value = response.data;
    })
    .catch(error => console.error(error));
}

function loadChocolates() {
  axios.get('http://localhost:8080/chocolate-factory/rest/chocolates/getAllForFactory/' + factoryId)
    .then(response => {
      chocolates.value = response.data.filter(chocolate => !chocolate.deleted);
      chocolates.value.forEach(chocolate => {
        chocolate.newQuantity = chocolate.quantity;
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
  if (chocolate) {
    axios.put(`http://localhost:8080/chocolate-factory/rest/chocolates/${chocolateId}`, chocolate)
      .then(response => {
        chocolate.quantity = chocolate.newQuantity;
      })
      .catch(error => console.error(error));
  }
}

function formatWorkingHours(workingHours) {
  return `${workingHours.startTime.slice(0, 5)} - ${workingHours.endTime.slice(0, 5)}`;
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
  transition: transform 0.2s;
}

.chocolate-card:hover {
  transform: scale(1.05);
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

.chocolate-price {
  color: #523F31;
  margin: 0 0 5px;
  font-size: 18px;
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
  margin-left: 10px;
  border-radius: 5px;
  cursor: pointer;
}

.btn-done:hover {
  background-color: #796254;
}


input[type="number"] {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 5px;
  width: 80px;
  box-sizing: border-box;
}

.edit-quantity-section {
  margin-top: 10px;
  width: 100%;
}
</style>
