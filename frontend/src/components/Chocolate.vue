<template>
  <div class="container">
    <div v-if="factory" class="factory-info">
      <div class="factory-details-box">
        <div class="factory-details">
          <p v-if="factory.workingHours">
            <strong>Working Hours:</strong>
            {{ formatWorkingHours(factory.workingHours) }}
          </p>
          <p><strong>Status:</strong> {{ factory.factoryStatus }}</p>
          <p><strong>Location:</strong> {{ factory.city }}, {{ factory.country }}</p>
          <p v-if="factory.averageRating"><strong>Rating:</strong> {{ factory.averageRating }}</p>
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
          <p><strong>Price:</strong> {{ chocolate.price }}</p>
          <p><strong>Type:</strong> {{ chocolate.type }}</p>
          <p><strong>Kind:</strong> {{ chocolate.kind }}</p>
          <p><strong>Weight:</strong> {{ chocolate.weight }} g</p>
          <p class="chocolate-description"><strong>Description:</strong> {{ chocolate.description }}</p>
          <div class="buttons">
            <button class="btn btn-delete" @click="deleteChocolate(chocolate.id)">Delete</button>
            <button class="btn btn-edit" @click="editChocolate(chocolate.id)">Edit</button>
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
  axios.get('http://localhost:8080/chocolate-factory/rest/factories/' + factoryId)
    .then(response => {
      factory.value = response.data;
    })
    .catch(error => console.error(error));
}

function loadChocolates() {
  axios.get('http://localhost:8080/chocolate-factory/rest/chocolates/getAllForFactory/' + factoryId)
    .then(response => {
      chocolates.value = response.data;
    })
    .catch(error => console.error(error));
}

function deleteChocolate(chocolateId) {
  axios.delete('http://localhost:8080/chocolate-factory/rest/chocolates/' + chocolateId)
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
  width: 300px;
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

.chocolate-card p {
  margin: 5px 0;
}

.chocolate-description {
  flex-grow: 1;
  margin-bottom: 10px;
  font-family: "Poppins", sans-serif;
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
  margin-right: 10px;
  font-family: "Poppins", sans-serif;
}

.btn-edit {
  background-color: #523F31;
  font-family: "Poppins", sans-serif;
}

.buttons {
  display: flex;
  justify-content: center;
}


</style>