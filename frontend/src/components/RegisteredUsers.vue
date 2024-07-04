<template>
  <div class="user-list-container">
    <div class="sidebar">
      <!-- Search bar -->
      <div class="search-container">
        <h4>SEARCH</h4>
        <div class="search-bar">
          <input v-model="searchCriteria.name" type="text" placeholder="Search by name" />
          <input v-model="searchCriteria.surname" type="text" placeholder="Search by surname" />
          <input v-model="searchCriteria.username" type="text" placeholder="Search by username" />
        </div>
        <div class="buttons">
          <button class="btn btn-search" @click="searchUsers">Search</button>
          <button class="btn btn-clear" @click="clearSearch">Clear</button>
        </div>
      </div>

      <div class="separator"></div>

      <!-- Filter by role -->
      <div class="filter-container">
        <h4>FILTER BY ROLE:</h4>
        <label>
          <input type="radio" v-model="selectedRole" value="" @change="filterByRole">
          All Roles
        </label>
        <label v-for="role in roles" :key="role">
          <input type="radio" v-model="selectedRole" :value="role" @change="filterByRole">
          {{ role }}
        </label>
      </div>
    </div>

    <!-- User list -->
    <div class="main-content">
      <div class="table-container">
        <table>
          <!-- Table headers -->
          <thead>
            <tr>
              <th @click="sortBy('username')" :class="{ 'sorted-asc': sortCriteria.usernameAsc, 'sorted-desc': sortCriteria.usernameDesc }">
                Username
                <span v-if="sortCriteria.usernameAsc"><i class="fas fa-sort-up"></i></span>
                <span v-if="sortCriteria.usernameDesc"><i class="fas fa-sort-down"></i></span>
              </th>
              <th @click="sortBy('name')" :class="{ 'sorted-asc': sortCriteria.nameAsc, 'sorted-desc': sortCriteria.nameDesc }">
                First Name
                <span v-if="sortCriteria.nameAsc"><i class="fas fa-sort-up"></i></span>
                <span v-if="sortCriteria.nameDesc"><i class="fas fa-sort-down"></i></span>
              </th>
              <th @click="sortBy('surname')" :class="{ 'sorted-asc': sortCriteria.surnameAsc, 'sorted-desc': sortCriteria.surnameDesc }">
                Last Name
                <span v-if="sortCriteria.surnameAsc"><i class="fas fa-sort-up"></i></span>
                <span v-if="sortCriteria.surnameDesc"><i class="fas fa-sort-down"></i></span>
              </th>
              <th>Gender</th>
              <th>Date of Birth</th>
              <th @click="sortBy('role')" :class="{ 'sorted-asc': sortCriteria.roleAsc, 'sorted-desc': sortCriteria.roleDesc }">
                Role
                <span v-if="sortCriteria.roleAsc"><i class="fas fa-sort-up"></i></span>
                <span v-if="sortCriteria.roleDesc"><i class="fas fa-sort-down"></i></span>
              </th>
              <th @click="sortBy('points')" :class="{ 'sorted-asc': sortCriteria.pointsAsc, 'sorted-desc': sortCriteria.pointsDesc }">
                Points
                <span v-if="sortCriteria.pointsAsc"><i class="fas fa-sort-up"></i></span>
                <span v-if="sortCriteria.pointsDesc"><i class="fas fa-sort-down"></i></span>
              </th>
            </tr>
          </thead>

          <!-- Table body -->
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
  nameAsc: true,
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
const roles = ['CUSTOMER', 'MANAGER', 'EMPLOYEE']; 

const selectedRole = ref(''); 

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
  axios.get('http://localhost:8080/chocolate-factory/rest/users/search', {
    params: {
      name: searchCriteria.value.name,
      surname: searchCriteria.value.surname,
      username: searchCriteria.value.username
    }
  })
  .then(response => {
    filteredUsers.value = response.data;
  })
  .catch(error => {
    console.error('Error searching users:', error);
  });
}

function clearSearch() {
  searchCriteria.value = {
    name: '',
    surname: '',
    username: ''
  };
  filteredUsers.value = users.value; 
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
  Object.keys(sortCriteria.value).forEach(criteria => {
    if (criteria !== `${key}Asc` && criteria !== `${key}Desc`) {
      sortCriteria.value[criteria] = false;
    }
  });

  if (sortCriteria.value[`${key}Asc`]) {
    sortCriteria.value[`${key}Desc`] = true;
    sortCriteria.value[`${key}Asc`] = false; 
  } else {
    sortCriteria.value[`${key}Asc`] = true;
    sortCriteria.value[`${key}Desc`] = false; 
  }

  const order = sortCriteria.value[`${key}Asc`] ? 'asc' : 'desc';
  axios.get(`http://localhost:8080/chocolate-factory/rest/users/sort`, {
    params: {
      sortBy: key,
      order: order
    }
  })
  .then(response => {
    filteredUsers.value = response.data;
  })
  .catch(error => {
    console.error('Error sorting users:', error);
  });
}

function formatDate(date) {
  const d = new Date(date);
  return d.toLocaleDateString();
}

function filterByRole() {
  if (selectedRole.value === '') {
    filteredUsers.value = users.value; 
  } else {
    axios.get(`http://localhost:8080/chocolate-factory/rest/users/filter`, {
      params: {
        role: selectedRole.value
      }
    })
    .then(response => {
      filteredUsers.value = response.data;
    })
    .catch(error => {
      console.error('Error filtering users by role:', error);
    });
  }
}

</script>

<style scoped>
.user-list-container {
  display: flex;
  justify-content: space-between;
}

.sidebar {
  width: 200px;
  padding: 20px;
  border-right: 1px solid #ccc;
}

.main-content {
  flex: 1;
  padding: 20px;
}

.search-container {
  margin-bottom: 20px;
  width: 100%;
}

.search-bar input {
  width: 90%;
  padding: 8px;
}

.buttons {
  margin-top: 10px;
}

.filter-container {
  margin-bottom: 20px;
  text-align: left;
}

.filter-container label {
  display: block;
  margin-bottom: 5px;
}

.table-container {
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  border: 1px solid #ccc;
  padding: 8px;
  text-align: left;
}

th {
  cursor: pointer;
  position: relative;
  background-color: #523F31; 
  color: white;
}

th.sorted-asc::after {
  content: '\25b2'; /* unicode for sorting */
  position: absolute;
  top: 50%;
  right: 8px;
  transform: translateY(-50%);
}

th.sorted-desc::after {
  content: '\25bc'; /* unicode for sorting */
  position: absolute;
  top: 50%;
  right: 8px;
  transform: translateY(-50%);
}
</style>
