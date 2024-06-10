<template>
  <div class="container">
    <header>
      <h1>{{ msg }}</h1>
      <h2>Our Factories: </h2>
    </header>
    <div class="factories-grid">
      <div v-for="factory in filtredFactories" :key="factory.id" class="factory-card">
        <img :src="factory.logo" alt="Logo" class="factory-logo" />
        <h3>{{ factory.name }}</h3>
        <p>{{ factory.city }}, {{ factory.country }}</p>
        <p class="status" :class="{'open': factory.factoryStatus === 'OPENED', 'closed': factory.factoryStatus === 'CLOSED'}">{{ formatStatus(factory.factoryStatus) }}</p>
        <p>Average Rating: {{ factory.averageRating }}</p>
        <button class="btn btn-view" @click="viewChocolates(factory.id)"> View chocolates </button>
      </div>
    </div>
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
  axios.get('http://localhost:8080/chocolate-factory/rest/factories/')
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
  text-align: center;
}

header h1 {
  margin-bottom: 15px;
  text-align: left;
}

header h2 {
  text-align: left;
  margin-left: 70px;
}

.factories-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
}

.factory-card {
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  width: 300px;
  text-align: center;
  padding: 20px;
  transition: transform 0.2s;
}

.factory-card:hover {
  transform: scale(1.05);
}

.factory-logo {
  width: 100%;
  height: 200px;
  object-fit: cover;
  object-position: center;
  border-radius: 5px;
}

.status {
  font-weight: bold;
}

.status.open {
  color: green;
}

.status.closed {
  color: red;
}

.btn {
  padding: 10px 20px;
  margin-top: 10px;
  cursor: pointer;
  border: none;
  border-radius: 5px;
  color: #fff;
  background-color: #3498db;
  transition: background-color 0.3s ease;
}

.btn:hover {
  background-color: #2980b9;
}
</style>
