<template>
  <div class="container">
    <header>
      <h1>{{ msg }}</h1>
      <h2>FABRIKE ČOKOLADE</h2>
    </header>
    <table class="factories-table">
      <thead>
        <tr>
          <th>Logo</th>
          <th>Name</th>
          <th>City</th>
          <th>Country</th>
          <th>Status</th>
          <th>Average Rating</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="factory in filtredFactories" :key="factory.id">
          <td><img :src="factory.logo" alt="Logo" width="50" /></td>
          <td>{{ factory.name }}</td>
          <td>{{ factory.city }}</td>
          <td>{{ factory.country }}</td>
          <td>{{ formatStatus(factory.factoryStatus) }}</td>
          <td>{{ factory.averageRating }}</td>
          <td><button class="btn btn-view" @click="viewChocolates(factory.id)"> View chocolates </button></td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();

defineProps({
  msg: {
    type: String,
    required: true
  }
});

const factories = ref([]);
const filtredFactories = ref([]);

onMounted(() => {
  loadFactories();
});

function loadFactories() {
  axios.get('http://localhost:8080/backend/rest/factories/')
    .then(response => {
      factories.value = response.data.sort((a, b) => {
        if (a.factoryStatus === 'OPENED' && b.factoryStatus !== 'OPENED') return -1;
        if (a.factoryStatus !== 'OPENED' && b.factoryStatus === 'OPENED') return 1;
        return 0;
      });
      filtredFactories.value = factories.value;
    })
    .catch(error => console.error(error));
}

function viewChocolates(factoryId) {
  router.push({ name: 'chocolates', params: { factoryId } });
}

function formatStatus(status) {
  const statusMap = {
    'OPENED': 'Opened',
    'CLOSED': 'Closed',
    // Add other statuses if needed
  };
  return statusMap[status] || status;
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

header h1 {
  margin-bottom: 10px;
}

header h2 {
  margin-top: 0;
  margin-bottom: 30px; }

.factories-table {
  width: 100%;
  border-collapse: collapse;
  margin: 20px 0;
  font-size: 1em;
  font-family: 'Arial', sans-serif;
  min-width: 400px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  overflow: hidden;
}

.factories-table thead tr {
  background-color: #009879;
  color: #ffffff;
  text-align: left;
  font-weight: bold;
}

.factories-table th, .factories-table td {
  padding: 12px 15px;
  border: 1px solid #ddd;
}

.factories-table tbody tr {
  border-bottom: 1px solid #ddd;
}

.factories-table tbody tr:nth-of-type(even) {
  background-color: #f3f3f3;
}

.factories-table tbody tr:last-of-type {
  border-bottom: 2px solid #009879;
}

img {
  max-width: 100%;
  height: auto;
}

.btn {
  padding: 8px 12px;
  margin: 2px;
  cursor: pointer;
  border: none;
  border-radius: 4px;
  color: #fff;
  text-align: center;
  background-color: #3498db;
  transition: background-color 0.3s ease;
}

.btn:hover {
  background-color: #2980b9;
}

.btn-view {
  background-color: #3498db;
}

.btn-view:hover {
  background-color: #2980b9;
}
</style>
