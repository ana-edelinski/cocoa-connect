<template>
  <div class="container">
    <header>
      <h1 class="factory-name">{{ factory ? factory.name : msg }}</h1>
    </header>

    <div v-if="factory" class="factory-info">
      <div class="factory-details-box">
        <div class="factory-details">
          <h2>Factory Information</h2>
          <p><strong>Name:</strong> {{ factory.name }}</p>
          <p v-if="factory.workingHours">
            <strong>Working Hours:</strong>
            {{ formatWorkingHours(factory.workingHours) }}
          </p>
          <p><strong>Status:</strong> {{ factory.factoryStatus }}</p>
          <p><strong>Location:</strong> {{ factory.city }}, {{ factory.country }}</p>
          <p v-if="factory.averageRating"><strong>Rating:</strong> {{ factory.averageRating }}</p>
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
      <h2>Chocolates</h2>
      <table class="chocolates-table">
        <thead>
          <tr>
            <th>Image</th>
            <th>Name</th>
            <th>Price</th>
            <th>Type</th>
            <th>Kind</th>
            <th>Weight</th>
            <th>Description</th>
            <th></th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="chocolate in chocolates" :key="chocolate.id">
            <td><img :src="chocolate.image" alt="Image" width="50" /></td>
            <td>{{ chocolate.name }}</td>
            <td>{{ chocolate.price }}</td>
            <td>{{ chocolate.type }}</td>
            <td>{{ chocolate.kind }}</td>
            <td>{{ chocolate.weight }} g</td>
            <td>{{ chocolate.description }}</td>
            <td>
              <button class="btn btn-delete" @click="deleteChocolate(chocolate.id)">Delete</button>
            </td>
            <td>
              <button class="btn btn-edit" @click="editChocolate(chocolate.id)">Edit</button>
            </td>
          </tr>
        </tbody>
      </table>
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

defineProps({
  msg: {
    type: String,
    required: true
  }
});

const factory = ref(null);
const chocolates = ref([]);

onMounted(() => {
  loadFactory();
  loadChocolates();
});

function loadFactory() {
  axios.get('http://localhost:8080/backend/rest/factories/' + factoryId)
    .then(response => {
      factory.value = response.data;
    })
    .catch(error => console.error(error));
}

function loadChocolates() {
  axios.get('http://localhost:8080/backend/rest/chocolates/getAllForFactory/' + factoryId)
    .then(response => {
      chocolates.value = response.data;
    })
    .catch(error => console.error(error));
}

function deleteChocolate(chocolateId) {
  axios.delete('http://localhost:8080/backend/rest/chocolates/' + chocolateId)
    .then(() => {
      chocolates.value = chocolates.value.filter(chocolate => chocolate.id !== chocolateId);
    })
    .catch(error => console.error(error));
}

function editChocolate(chocolateId) {
  router.push({ name: 'editChocolate', params: { chocolateId } });
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
  font-family: 'Arial', sans-serif;
}

header {
  text-align: center;
  margin-bottom: 20px;
}

.factory-name {
  color: #800080; /* Purple color for the factory name */
}

.factory-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 50px;
  margin-left: 170px;
  margin-up: 50;

}

.factory-details-box {
  display: flex;
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

.factory-logo img {
  max-width: 100%;
  height: auto;
  width: 200px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.chocolates-section {
  margin-top: 20px;
}

.chocolates-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.chocolates-table thead tr {
  background-color: #009879;
  color: #ffffff;
  text-align: left;
  font-weight: bold;
}

.chocolates-table th, .chocolates-table td {
  padding: 12px 15px;
  border: 1px solid #ddd;
}

.chocolates-table tbody tr {
  border-bottom: 1px solid #ddd;
}

.chocolates-table tbody tr:nth-of-type(even) {
  background-color: #f3f3f3;
}

.chocolates-table tbody tr:last-of-type {
  border-bottom: 2px solid #009879;
}

.btn {
  padding: 8px 12px;
  margin: 2px;
  cursor: pointer;
  border: none;
  border-radius: 4px;
  color: #fff;
  text-align: center;
}

.btn-delete {
  background-color: #e74c3c;
}

.btn-edit {
  background-color: #3498db;
}
</style>
