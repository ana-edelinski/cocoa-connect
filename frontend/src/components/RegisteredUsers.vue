<template>
  <div class="user-list-container">
    <h1>Registered Users</h1>
    <div class="search-container">
      <div class="search-bar">
        <div class="search-inputs">
          <input v-model="searchCriteria.name" type="text" placeholder="Search by name" />
          <input v-model="searchCriteria.surname" type="text" placeholder="Search by surname" />
          <input v-model="searchCriteria.username" type="text" placeholder="Search by username" />
        </div>
        <div class="buttons">
          <button class="btn btn-search" @click="searchUsers">Search</button>
          <button class="btn btn-clear" @click="clearSearch">Clear</button>
        </div>
      </div>
    </div>
    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th @click="sortBy('username')">Username</th>
            <th @click="sortBy('name')">First Name</th>
            <th @click="sortBy('surname')">Last Name</th>
            <th>Gender</th>
            <th>Date of Birth</th>
            <th @click="sortBy('role')">Role</th>
            <th @click="sortBy('points')">Points</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in sortedUsers" :key="user.username">
            <td>{{ user.username }}</td>
            <td>{{ user.name }}</td>
            <td>{{ user.surname }}</td>
            <td>{{ user.gender }}</td>
            <td>{{ formatDate(user.dateOfBirth) }}</td>
            <td>{{ user.role }}</td>
            <td>{{ user.points }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';

const users = ref([]);
const filteredUsers = ref([]);
const searchCriteria = ref({
  name: '',
  surname: '',
  username: ''
});
const sortCriteria = ref({
  nameAsc: false,
  nameDesc: false,
  surnameAsc: false,
  surnameDesc: false,
  usernameAsc: false,
  usernameDesc: false,
  roleAsc: false,
  roleDesc: false,
  pointsAsc: false,
  pointsDesc: false
});

onMounted(() => {
  fetchUsers();
});

function fetchUsers() {
  axios.get('http://localhost:8080/chocolate-factory/rest/users/')
    .then(response => {
      users.value = response.data;
      filteredUsers.value = response.data; 
    })
    .catch(error => {
      console.error('Error fetching users:', error);
    });
}

function searchUsers() {
  filteredUsers.value = users.value.filter(user => {
    const matchesName = user.name.toLowerCase().includes(searchCriteria.value.name.toLowerCase());
    const matchesSurname = user.surname.toLowerCase().includes(searchCriteria.value.surname.toLowerCase());
    const matchesUsername = user.username.toLowerCase().includes(searchCriteria.value.username.toLowerCase());

    return matchesName && matchesSurname && matchesUsername;
  });
}

function clearSearch() {
  searchCriteria.value = {
    name: '',
    surname: '',
    username: ''
  };
  filteredUsers.value = users.value; // Reset to show all users
}

const sortedUsers = computed(() => {
  let sorted = [...filteredUsers.value];

  if (sortCriteria.value.nameAsc) {
    sorted.sort((a, b) => a.name.localeCompare(b.name));
  }
  if (sortCriteria.value.nameDesc) {
    sorted.sort((a, b) => b.name.localeCompare(a.name));
  }
  if (sortCriteria.value.surnameAsc) {
    sorted.sort((a, b) => a.surname.localeCompare(b.surname));
  }
  if (sortCriteria.value.surnameDesc) {
    sorted.sort((a, b) => b.surname.localeCompare(a.surname));
  }
  if (sortCriteria.value.usernameAsc) {
    sorted.sort((a, b) => a.username.localeCompare(b.username));
  }
  if (sortCriteria.value.usernameDesc) {
    sorted.sort((a, b) => b.username.localeCompare(a.username));
  }
  if (sortCriteria.value.roleAsc) {
    sorted.sort((a, b) => a.role.localeCompare(b.role));
  }
  if (sortCriteria.value.roleDesc) {
    sorted.sort((a, b) => b.role.localeCompare(a.role));
  }
  if (sortCriteria.value.pointsAsc) {
    sorted.sort((a, b) => a.points - b.points);
  }
  if (sortCriteria.value.pointsDesc) {
    sorted.sort((a, b) => b.points - a.points);
  }

  return sorted;
});

function sortBy(key) {
  // Reset all sort criteria first
  Object.keys(sortCriteria.value).forEach(criteria => {
    sortCriteria.value[criteria] = false;
  });

  // Determine the sorting order for the selected key
  if (sortCriteria.value[`${key}Asc`]) {
    sortCriteria.value[`${key}Desc`] = true;
  } else {
    sortCriteria.value[`${key}Asc`] = true;
  }
}

function formatDate(date) {
  const d = new Date(date);
  return d.toLocaleDateString();
}
</script>

<style scoped>
.user-list-container {
  max-width: 800px;
  margin: 0 auto;
}

h1 {
  text-align: center;
  color: #523F31;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

th, td {
  padding: 10px;
  text-align: center; /* Center content in cells */
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #523F31;
  color: white;
  cursor: pointer; /* Add cursor pointer to indicate clickable headers */
}

tr:hover {
  background-color: #f1f1f1;
}

tbody tr:last-child td {
  border-bottom: 2px solid #523F31; /* Thicker border for the last row */
}

.table-container {
  margin: 0 auto;
}

.search-container {
  display: flex;
  align-items: center;
  gap: 10px;
  justify-content: space-between;
  margin-left: 30px;
}

.search-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.search-inputs {
  display: flex;
  margin: 0 auto;
  gap: 10px;
  justify-content: space-between;
}

.buttons {
  margin-left: 10px; /* Adjust the margin between inputs and buttons */
}
</style>
