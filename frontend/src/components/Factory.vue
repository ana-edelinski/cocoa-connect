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
        <div class="separator">

        </div>
        <div class="filter-container">
          <div class="filter-bar">
            <h4>FILTER BY CHOCOLATE TYPES</h4>
            <div class="filter-options">
              <label>
                <input type="radio" v-model="filterCriteria.chocolateType" value="" />
                All Chocolate Types
              </label>
              <label v-for="type in chocolateTypes" :key="type">
                <input type="radio" v-model="filterCriteria.chocolateType" :value="type" />
                {{ type }}
              </label>
            </div>
            <h4>FILTER BY CHOCOLATE KINDS</h4>
            <div class="filter-options">
              <label>
                <input type="radio" v-model="filterCriteria.chocolateKind" value="" />
                All Chocolate Kinds
              </label>
              <label v-for="kind in chocolateKinds" :key="kind">
                <input type="radio" v-model="filterCriteria.chocolateKind" :value="kind" />
                {{ kind }}
              </label>
            </div>
            <label class="filter-checkbox">
              <input type="checkbox" v-model="filterCriteria.openOnly" />
              Opened factories only
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
    openOnly: filterCriteria.value.openOnly ? filterCriteria.value.openOnly : null
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
}

function formatStatus(status) {
  return status === 'OPENED' ? 'Opened' : 'Closed';
}

function viewChocolates(factoryId) {
  router.push(`/chocolates/${factoryId}`);
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
  margin-right: 50px;
  width: 25%;
}

.search-container h4 {
  text-align: left;
  margin-bottom: 5px;
  font-weight: 500;
  font-size: 0.9rem;
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
  font-size: 0.9rem;
}

.search-bar .rating-range {
  width: fit-content;
  display: flex;
  gap: 10px;
}

.search-bar .rating-range input {
  display: flex;
  gap: 5px;
  justify-content: space-between;
  border: 1px solid #523F31;
  border-radius: 5px;
  width: 48%;
}

.filter-container {
  margin-bottom: 20px;
}

.filter-container h4 {
  text-align: left;
  margin-bottom: 5px;
  font-weight: 500;
  font-size: 0.9rem;
}

.filter-bar {
  display: flex;
  flex-direction: column;
  align-items: stretch;
}

.filter-bar .buttons {
  display: flex;
  justify-content: flex-start;
}

.filter-options {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.filter-options label {
  cursor: pointer;
  font-size: 0.9rem;
  text-align: left;
}

.filter-bar select, .filter-bar label {
  padding: 5px 0;
  border: none;
  font-family: "Poppins", sans-serif;
  height: fit-content;
  width: 100%;
}

.filter-bar .filter-checkbox {
  display: flex;
  align-items: center;
  padding: 0;
  border: none;
  font-family: "Poppins", sans-serif;
  font-size: 0.9rem;
  width: 100%;
  height: 50px;
}

.filter-checkbox input {
  margin-right: 10px;
  margin-left: 10px;
  height: fit-content;
}

.search-bar .buttons {
  display: flex;
  flex-direction: row;
  gap: 10px;
  justify-content: center;
}

.buttons .btn-search {
  color: white;
  background-color: #523F31;
  width: fit-content;
  font-size: small;
}

.buttons .btn-clear {
  color: white;
  background-color: #523F31;
  width: fit-content;
  font-size: small;}

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
  font-size: 0.9rem;
}

.btn-view {
  background-color: #523F31;
}

.btn-done {
  background-color: #523F31;
  width: fit-content;
  font-size: small;
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

.factory-card h3 {
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis; 
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
  white-space: nowrap; 
  justify-content: left;
  overflow: auto; 
  text-overflow: ellipsis; 
}

.separator {
  border-bottom: 1px solid #ddd; 
  padding-bottom: 10px; 
  margin-bottom: 10px; 
}

input[type="radio"]:checked::before,
input[type="checkbox"]:checked::before {
  content: "";
  display: inline-block;
  width: 12px;
  height: 12px;
  background-color: #523F31; 
  border-radius: 50%; 
  margin-right: 10px;
}

input[type="checkbox"]:checked::before {
  border-radius: 3px; 
}

input[type="radio"]:checked,
input[type="checkbox"]:checked {
  background-color: #523F31; 
  border-color: #523F31; 
}



</style>
