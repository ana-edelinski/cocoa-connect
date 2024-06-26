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
        <select v-model="searchCriteria.chocolateType">
          <option value="">All Chocolate Types</option>
          <option value="dark">Dark</option>
          <option value="milk">Milk</option>
          <option value="white">White</option>
        </select>
        <select v-model="searchCriteria.chocolateCategory">
          <option value="">All Chocolate Categories</option>
          <option value="bar">Bar</option>
          <option value="truffle">Truffle</option>
          <option value="bonbon">Bonbon</option>
        </select>
        <label class="filter-checkbox">
          <input type="checkbox" v-model="searchCriteria.openOnly" />
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
          <input type="checkbox" v-model="sortCriteria.nameAsc" />
          Sort by Name (Ascending)
        </label>
        <label>
          <input type="checkbox" v-model="sortCriteria.nameDesc" />
          Sort by Name (Descending)
        </label>
        <label>
          <input type="checkbox" v-model="sortCriteria.locationAsc" />
          Sort by Location (Ascending)
        </label>
        <label>
          <input type="checkbox" v-model="sortCriteria.locationDesc" />
          Sort by Location (Descending)
        </label>
        <label>
          <input type="checkbox" v-model="sortCriteria.ratingAsc" />
          Sort by Rating (Ascending)
        </label>
        <label>
          <input type="checkbox" v-model="sortCriteria.ratingDesc" />
          Sort by Rating (Descending)
        </label>
      </div>
    </div>
    <div class="factories-grid">
      <div v-for="factory in sortedAndFilteredFactories" :key="factory.id" class="factory-card">
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
import { ref, onMounted, computed } from 'vue';
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
const filteredFactories = ref([]);
const searchCriteria = ref({
  name: '',
  location: '',
  minRating: null,
  maxRating: null,
  chocolateType: '',
  chocolateCategory: '',
  openOnly: false,
});
const sortCriteria = ref({
  nameAsc: false,
  nameDesc: false,
  locationAsc: false,
  locationDesc: false,
  ratingAsc: false,
  ratingDesc: false,
});
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
  // Display all factories initially
  filteredFactories.value = factories.value;
});

function loadFactories() {
  axios.get('http://localhost:8080/chocolate-factory/rest/factories/')
    .then(response => {
      factories.value = response.data.sort((a, b) => {
        if (a.factoryStatus === 'OPENED' && b.factoryStatus !== 'OPENED') return -1;
        if (a.factoryStatus !== 'OPENED' && b.factoryStatus === 'OPENED') return 1;
        return 0;
      });
      filteredFactories.value = factories.value; // Display all factories initially
    })
    .catch(error => console.error(error));
}

function searchFactories() {
  filteredFactories.value = factories.value.filter(factory => {
    const matchesName = factory.name.toLowerCase().includes(searchCriteria.value.name.toLowerCase());
    const matchesLocation = factory.city.toLowerCase().includes(searchCriteria.value.location.toLowerCase()) || factory.country.toLowerCase().includes(searchCriteria.value.location.toLowerCase());
    const matchesMinRating = searchCriteria.value.minRating === null || factory.averageRating >= searchCriteria.value.minRating;
    const matchesMaxRating = searchCriteria.value.maxRating === null || factory.averageRating <= searchCriteria.value.maxRating;
    const matchesChocolateType = searchCriteria.value.chocolateType === '' || factory.chocolates.some(chocolate => chocolate.type.toLowerCase() === searchCriteria.value.chocolateType.toLowerCase());
    const matchesChocolateCategory = searchCriteria.value.chocolateCategory === '' || factory.chocolates.some(chocolate => chocolate.category.toLowerCase() === searchCriteria.value.chocolateCategory.toLowerCase());
    const matchesOpenStatus = !searchCriteria.value.openOnly || factory.factoryStatus === 'OPENED';

    return matchesName && matchesLocation && matchesMinRating && matchesMaxRating && matchesChocolateType && matchesChocolateCategory && matchesOpenStatus;
  });
}

function clearSearch() {
  searchCriteria.value = {
    name: '',
    location: '',
    minRating: null,
    maxRating: null,
    chocolateType: '',
    chocolateCategory: '',
    openOnly: false,
  };
  filteredFactories.value = factories.value; // Reset to show all factories
}

function toggleFilters() {
  showFilters.value = !showFilters.value;
}

function toggleSorting() {
  showSorting.value = !showSorting.value;
}

function applyFilters() {
  searchFactories();
  showFilters.value = false;
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

const sortedAndFilteredFactories = computed(() => {
  let sorted = [...filteredFactories.value];

  if (sortCriteria.value.nameAsc) {
    sorted.sort((a, b) => a.name.localeCompare(b.name));
  }
  if (sortCriteria.value.nameDesc) {
    sorted.sort((a, b) => b.name.localeCompare(a.name));
  }
  if (sortCriteria.value.locationAsc) {
    sorted.sort((a, b) => a.city.localeCompare(b.city) || a.country.localeCompare(b.country));
  }
  if (sortCriteria.value.locationDesc) {
    sorted.sort((a, b) => b.city.localeCompare(a.city) || b.country.localeCompare(a.country));
  }
  if (sortCriteria.value.ratingAsc) {
    sorted.sort((a, b) => a.averageRating - b.averageRating);
  }
  if (sortCriteria.value.ratingDesc) {
    sorted.sort((a, b) => b.averageRating - a.averageRating);
  }
  
  return sorted;
});
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
