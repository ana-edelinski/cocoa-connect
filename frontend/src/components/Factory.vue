
<template>
  <div class="container">
    <header>
      <h1>{{ msg }}</h1>
      <h2>Our Factories</h2>
    </header>
    <div class="search-container">
      <div class="search-bar">
        <input v-model="searchCriteria.name" type="text" placeholder="Search by factory name" />
        <input v-model="searchCriteria.location" type="text" placeholder="Search by city or country" />
        <input v-model="searchCriteria.chocolateName" type="text" placeholder="Search by chocolate name" />
        <div class="rating-range">
          <input v-model.number="searchCriteria.minRating" type="number" step="0.1" placeholder="Min rating" />
          <input v-model.number="searchCriteria.maxRating" type="number" step="0.1" placeholder="Max rating" />
        </div>
        <div class="buttons">
          <button class="btn btn-search" @click="searchFactories">Search</button>
          <button class="btn btn-clear" @click="clearSearch">Clear</button>
        </div>
      </div>
    </div>
    <div class="sort-filter-buttons">
      <button class="btn btn-filter" @click="toggleFilters">Filter</button>
      <button class="btn btn-sort" @click="toggleSorting">Sort</button>
    </div>
    <div v-if="showFilters" class="filter-container">
      <div class="filter-bar">
        <select v-model="filterCriteria.chocolateType">
          <option value="">All Chocolate Types</option>
          <option v-for="type in chocolateTypes" :key="type" :value="type">{{ type }}</option>
        </select>
        <select v-model="filterCriteria.chocolateKind">
          <option value="">All Chocolate Kinds</option>
          <option v-for="kind in chocolateKinds" :key="kind" :value="kind">{{ kind }}</option>
        </select>
        <label class="filter-checkbox">
          <input type="checkbox" v-model="filterCriteria.openOnly" />
          Show only open factories
        </label>
        <div class="buttons">
          <button class="btn btn-done" @click="applyFilters">Done</button>
        </div>
      </div>
    </div>
    <div v-if="showSorting" class="sorting-container">
      <div class="sorting-buttons">
        <label>
          <input type="radio" v-model="sortCriteria.sortBy" value="name" />
          Sort by Name
        </label>
        <label>
          <input type="radio" v-model="sortCriteria.sortBy" value="location" />
          Sort by Location
        </label>
        <label>
          <input type="radio" v-model="sortCriteria.sortBy" value="rating" />
          Sort by Rating
        </label>
        <label>
          <input type="radio" v-model="sortCriteria.order" value="asc" />
          Ascending
        </label>
        <label>
          <input type="radio" v-model="sortCriteria.order" value="desc" />
          Descending
        </label>
        <div class="buttons">
          <button class="btn btn-done" @click="applySorting">Done</button>
          <button class="btn btn-clear" @click="clearSort">Clear Sort</button>
        </div>
      </div>
    </div>
    <div class="factories-grid">
      <div v-for="factory in factories" :key="factory.id" class="factory-card">
        <img :src="factory.logo" alt="Logo" class="factory-logo" />
        <h3>{{ factory.name }}</h3>
        <p>{{ factory.city }}, {{ factory.country }}</p>
        <p>Average Rating: {{ factory.averageRating }}</p>
        <p class="status" :class="{'open': factory.factoryStatus === 'OPENED', 'closed': factory.factoryStatus === 'CLOSED'}">{{ formatStatus(factory.factoryStatus) }}</p>
        <button class="btn btn-view" @click="viewChocolates(factory.id)">PREVIEW</button>
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
const searchCriteria = ref({
  name: '',
  location: '',
  chocolateName: '',
  minRating: null,
  maxRating: null,
});
const filterCriteria = ref({
  chocolateType: '',
  chocolateKind: '',
  openOnly: null,
});
const sortCriteria = ref({
  sortBy: '',
  order: '',
});
const chocolateTypes = ref([]);
const chocolateKinds = ref([]);
const loggedUser = ref(null);
const showFilters = ref(false);
const showSorting = ref(false);

onMounted(() => {
  // Load logged user from local storage or API
  const storedUser = localStorage.getItem('loggedUser');
  if (storedUser) {
    loggedUser.value = JSON.parse(storedUser);
  }

  loadFactories();
  loadChocolateTypes();
  loadChocolateKinds();
});

function loadFactories() {
  axios.get('http://localhost:8080/backend/rest/factories/')
    .then(response => {
      factories.value = response.data.sort((a, b) => {
        if (a.factoryStatus === 'OPENED' && b.factoryStatus !== 'OPENED') return -1;
        if (a.factoryStatus !== 'OPENED' && b.factoryStatus === 'OPENED') return 1;
        return 0;
      });
    })
    .catch(error => console.error(error));
}

function loadChocolateTypes() {
  axios.get('http://localhost:8080/backend/rest/factories/chocolateTypes')
    .then(response => {
      chocolateTypes.value = response.data;
    })
    .catch(error => console.error(error));
}

function loadChocolateKinds() {
  axios.get('http://localhost:8080/backend/rest/factories/chocolateKinds')
    .then(response => {
      chocolateKinds.value = response.data;
    })
    .catch(error => console.error(error));
}

function searchFactories() {
  const params = {
    name: searchCriteria.value.name,
    chocolateName: searchCriteria.value.chocolateName,
    location: searchCriteria.value.location,
    minRating: searchCriteria.value.minRating,
    maxRating: searchCriteria.value.maxRating
  };

  axios.get('http://localhost:8080/backend/rest/factories/search', { params })
    .then(response => {
      factories.value = response.data;
    })
    .catch(error => console.error(error));
}

function applyFilters() {
  const params = {
    chocolateType: filterCriteria.value.chocolateType,
    chocolateKind: filterCriteria.value.chocolateKind,
    openOnly: filterCriteria.value.openOnly
  };

  axios.get('http://localhost:8080/backend/rest/factories/filter', { params })
    .then(response => {
      factories.value = response.data.sort((a, b) => {
        if (a.factoryStatus === 'OPENED' && b.factoryStatus !== 'OPENED') return -1;
        if (a.factoryStatus !== 'OPENED' && b.factoryStatus === 'OPENED') return 1;
        return 0;
      });
    })
    .catch(error => console.error(error));
}

function applySorting() {
  const params = {
    sortBy: sortCriteria.value.sortBy,
    order: sortCriteria.value.order
  };

  axios.get('http://localhost:8080/backend/rest/factories/sort', { params })
    .then(response => {
      factories.value = response.data;
    })
    .catch(error => console.error(error));
}

function clearSearch() {
  searchCriteria.value = {
    name: '',
    location: '',
    chocolateName: '',
    minRating: null,
    maxRating: null,
  };
  loadFactories(); // Reload all factories
}

function clearSort() {
  sortCriteria.value = {
    sortBy: '',
    order: '',
  };
  loadFactories(); // Reload all factories
}

function toggleFilters() {
  showFilters.value = !showFilters.value;
}

function toggleSorting() {
  showSorting.value = !showSorting.value;
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
  font-family: "Poppins", sans-serif;
  text-align: center;
}

header h1 {
  margin-bottom: 15px;
  text-align: left;
  font-size: 2.5rem;
  color: #333;
}

header h2 {
  text-align: left;
  margin-left: 100px;
  font-size: 2rem;
  color: #2D1E17;
  font-family: "Poppins", sans-serif;
}

.search-container {
  padding: 20px;
  margin: 0 auto;
  margin-bottom: 20px;
  width: 1000px;
}

.search-bar {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.search-bar input, .search-bar select {
  padding: 10px;
  border: 1px solid #523F31;
  border-radius: 5px;
  font-family: "Poppins", sans-serif;
  font-size: 1rem;
}

.rating-range {
  display: flex;
  gap: 10px;
  justify-content: space-between;
}

.rating-range input {
  width: 48%;
}

.filter-container {
  border: 2px solid #523F31; /* Brown color border */
  padding: 20px;
  border-radius: 10px;
  margin-bottom: 20px;
  background-color: #f9f9f9;
}

.filter-bar {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.filter-bar select, .filter-bar label {
  padding: 15px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-family: "Poppins", sans-serif;
  font-size: 1rem;
}

.filter-checkbox {
  display: flex;
  align-items: center;
}

.filter-checkbox input {
  margin-right: 10px;
}

.buttons {
  display: flex;
  gap: 10px;
  justify-content: center;
  margin-top: 10px;
}

.sort-filter-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin: 10px;
  margin-right: 100px;
  margin-bottom: 30px;
  
}

.btn {
  padding: 12px 20px;
  cursor: pointer;
  border: none;
  border-radius: 5px;
  color: #fff;
  font-weight: 500;
  font-family: "Poppins", sans-serif;
  transition: background-color 0.3s ease, transform 0.2s;
  font-size: 1rem;
}

.btn-search {
  background-color: #523F31;
}

.btn-clear {
  background-color: #ccc;
  color: #523F31;
}

.btn-filter {
  background-color: #8B4513;
}

.btn-sort {
  background-color: #8B4513;
}

.btn-view {
  background-color: #523F31;
}

.btn-done {
  background-color: #8B4513;
  margin-top: 10px;
}

.btn:hover {
  transform: scale(1.05);
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
  font-family: "Poppins", sans-serif;
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

.sorting-container {
  margin-top: 20px;
}

.sorting-buttons label {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.sorting-buttons label input {
  margin-right: 10px;
}
</style>
