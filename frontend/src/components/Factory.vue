<template>
  <div class="container">
    <header>
      <h1>{{ msg }}</h1>
    </header>
    <div class="content">
      <div class="search-container">
        <h4>SEARCH</h4>
        <div class="search-bar">
          <input v-model="searchCriteria.name" type="text" placeholder="Search by factory name" />
          <input v-model="searchCriteria.location" type="text" placeholder="Search by city or country" />
          <input v-model="searchCriteria.chocolateName" type="text" placeholder="Search by chocolate name" />
          <div class="rating-range">
            <input v-model.number="searchCriteria.minRating" type="number" step="0.1" placeholder="Min rating" />
            <input v-model.number="searchCriteria.maxRating" type="number" step="0.1" placeholder="Max rating" />
          </div>
          <div class="buttons">
            <button class="btn btn-search" @click="searchFactories">SEARCH</button>
            <button class="btn btn-clear" @click="clearSearch">RESET</button>
          </div>
        </div>
        <div class="filter-container">
          <h4>FILTER</h4>

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
              <button class="btn btn-done" @click="applyFilters">APPLY FILTERS</button>
            </div>
          </div>
        </div>
      </div>
      <div class="factories-container">
        <div class="sort-filter-buttons">
          <h2>Our Factories</h2>
          <select v-model="sortCriteria.sortBy" @change="applySorting">
            <option value="">Default Sorting</option>
            <option value="name-asc">Sort by Name: Ascending</option>
            <option value="name-desc">Sort by Name: Descending</option>
            <option value="location-asc">Sort by Location: Ascending</option>
            <option value="location-desc">Sort by Location: Descending</option>
            <option value="rating-asc">Sort by Rating: Ascending</option>
            <option value="rating-desc">Sort by Rating: Descending</option>
          </select>
        </div>
        <div class="factories-grid">
          <div v-for="factory in sortedFactories" :key="factory.id" class="factory-card">
            <img :src="factory.logo" alt="Logo" class="factory-logo" />
            <h3>{{ factory.name }}</h3>
            <p>{{ factory.city }}, {{ factory.country }}</p>
            <p>Average Rating: {{ factory.averageRating }}</p>
            <p class="status" :class="{'open': factory.factoryStatus === 'OPENED', 'closed': factory.factoryStatus === 'CLOSED'}">{{ formatStatus(factory.factoryStatus) }}</p>
            <button class="btn btn-view" @click="viewChocolates(factory.id)">PREVIEW</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
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
});
const chocolateTypes = ref([]);
const chocolateKinds = ref([]);
const loggedUser = ref(null);

onMounted(() => {
  const storedUser = localStorage.getItem('loggedUser');
  if (storedUser) {
    loggedUser.value = JSON.parse(storedUser);
  }

  loadFactories();
  loadChocolateTypes();
  loadChocolateKinds();
});

const sortedFactories = computed(() => {
  let sorted = [...factories.value];
  if (sortCriteria.value.sortBy) {
    const [sortBy, order] = sortCriteria.value.sortBy.split('-');
    sorted.sort((a, b) => {
      let compareA, compareB;
      if (sortBy === 'name') {
        compareA = a.name.toLowerCase();
        compareB = b.name.toLowerCase();
      } else if (sortBy === 'location') {
        compareA = `${a.city.toLowerCase()}, ${a.country.toLowerCase()}`;
        compareB = `${b.city.toLowerCase()}, ${b.country.toLowerCase()}`;
      } else if (sortBy === 'rating') {
        compareA = a.averageRating;
        compareB = b.averageRating;
      }
      if (compareA < compareB) return order === 'asc' ? -1 : 1;
      if (compareA > compareB) return order === 'asc' ? 1 : -1;
      return 0;
    });
  }
  return sorted;
});

function loadFactories() {
  axios.get('http://localhost:8080/chocolate-factory/rest/factories/')
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
  axios.get('http://localhost:8080/chocolate-factory/rest/factories/chocolateTypes')
    .then(response => {
      chocolateTypes.value = response.data;
    })
    .catch(error => console.error(error));
}

function loadChocolateKinds() {
  axios.get('http://localhost:8080/chocolate-factory/rest/factories/chocolateKinds')
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

  axios.get('http://localhost:8080/chocolate-factory/rest/factories/search', { params })
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

  axios.get('http://localhost:8080/chocolate-factory/rest/factories/filter', { params })
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
    order: sortCriteria.value.sortBy.endsWith('-asc') ? 'asc' : 'desc'
  };

  axios.get('http://localhost:8080/chocolate-factory/rest/factories/sort', { params })
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

function viewChocolates(factoryId) {
  router.push({ name: 'chocolates', params: { factoryId } });
}

function formatStatus(status) {
  const statusMap = {
    'OPENED': 'Opened',
    'CLOSED': 'Closed',
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

.content {
  display: flex;
  justify-content: space-between;
}

.search-container {
  margin-top: 10px;
  margin-right: 20px;
  width: 25%;
}

.search-container h4 {
  text-align: left;
  margin-bottom: 5px;
  font-weight: 500;
}

.factories-container {
  width: 75%;
}

.search-bar {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 20px;
}

.search-bar input, .search-bar select {
  padding: 10px;
  border: 1px solid #523F31;
  border-radius: 5px;
  font-family: "Poppins", sans-serif;
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
  padding: 20px;
  margin-bottom: 20px;
}

.filter-container h4 {
  text-align: left;
  margin-bottom: 5px;
  font-weight: 500;
}

.filter-bar {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.filter-bar select, .filter-bar label {
  padding: 15px;
  border: 1px solid #523F31;
  border-radius: 5px;
  font-family: "Poppins", sans-serif;
  height: fit-content;
}

.filter-checkbox {
  display: flex;
  align-items: center;
  padding: 15px;
  border: 1px solid #523F31;
  border-radius: 5px;
  font-family: "Poppins", sans-serif;
  font-size: small; 
}

.filter-checkbox input {
  margin-right: 10px;
  height: fit-content;

}

.buttons {
  display: flex;
  gap: 10px;
  justify-content: center;
}

.sort-filter-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin: 10px;
  margin-right: 30px;
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
  font-size: small;
}

.btn-search {
  background-color: #523F31;
  font-size: small;
}

.btn-clear {
  background-color: #ccc;
  color: #523F31;
  font-size: small;
}



.btn-view {
  background-color: #523F31;
}

.btn-done {
  background-color: #523F31;
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
  width: 240px;
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

.sort-filter-buttons select {
  height: 25px;
  margin-top: 20px;
}

.sort-filter-buttons h2 {
  margin-right: 470px;
}
</style>
