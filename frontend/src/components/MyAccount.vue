<template>
  <div class="my-account">
    <div class="tabs">
      <button :class="{ active: currentTab === 'profile' }" @click="currentTab = 'profile'">MY PROFILE</button>
      <button v-if="loggedUser && loggedUser.role === 'CUSTOMER'" :class="{ active: currentTab === 'orders' }" @click="currentTab = 'orders'">MY ORDERS</button>
      <button v-if="loggedUser && loggedUser.role === 'MANAGER'" :class="{ active: currentTab === 'cancelRequests' }" @click="currentTab = 'cancelRequests'">CANCEL REQUESTS</button>
    </div>
    <div class="tab-content" v-if="currentTab === 'profile'">
      <h2>Profile Information</h2>
      <form @submit.prevent="saveProfile">
        <label>
          Username:
          <input type="text" v-model="profile.username" required>
        </label>
        <label>
          First Name:
          <input type="text" v-model="profile.firstName" required>
        </label>
        <label>
          Last Name:
          <input type="text" v-model="profile.lastName" required>
        </label>
        <label>
          Gender:
          <select v-model="profile.gender" required>
            <option value="MALE">MALE</option>
            <option value="FEMALE">FEMALE</option>
          </select>
        </label>
        <label>
          Birthday:
          <input type="date" v-model="profile.birthday" required>
        </label>
        <div class="buttons">
          <button type="submit">Save</button>
          <button type="button" @click="changePasswordModalVisible = true">Change Password</button>
        </div>
      </form>
    </div>
    <div class="tab-content" v-if="currentTab === 'orders'">
      <h2>My Orders</h2>
      <table>
        <tr>
          <th>Order ID</th>
          <th>Date</th>
          <th>Status</th>
        </tr>
        <tr v-for="order in orders" :key="order.id">
          <td>{{ order.id }}</td>
          <td>{{ order.date }}</td>
          <td>{{ order.status }}</td>
        </tr>
      </table>
    </div>
    <div class="tab-content" v-if="currentTab === 'cancelRequests'">
      <h2>Cancel Requests</h2>
      <table>
        <tr>
          <th>Request ID</th>
          <th>Order ID</th>
          <th>Date</th>
          <th>Status</th>
        </tr>
        <tr v-for="request in cancelRequests" :key="request.id">
          <td>{{ request.id }}</td>
          <td>{{ request.orderId }}</td>
          <td>{{ request.date }}</td>
          <td>{{ request.status }}</td>
        </tr>
      </table>
    </div>
  </div>

  <div class="modal" v-if="changePasswordModalVisible">
    <div class="modal-content">
      <span class="close" @click="changePasswordModalVisible = false">&times;</span>
      <h2>Change Password</h2>
      <form @submit.prevent="changePassword">
        <label>
          Current Password:
          <input type="password" v-model="currentPassword" required>
        </label>
        <div v-if="passwordChangeError">
          <span style="color: red;">{{ passwordChangeError }}</span>
        </div>
        <label>
          New Password:
          <input type="password" v-model="newPassword" required>
        </label>
        <label>
          Re-Type New Password:
          <input type="password" v-model="confirmPassword" required>
        </label>
        
        <div v-if="newPassword !== confirmPassword">
          <span style="color: red;">Passwords do not match.</span>
        </div>
        
        <button type="submit">Save</button>
      </form>

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import axios from 'axios';

const loggedUser = ref(null);
const profile = ref({});
const orders = ref([]);
const cancelRequests = ref([]);
const currentTab = ref('profile');
const changePasswordModalVisible = ref(false);
let currentPassword = ref('');
let newPassword = ref('');
let confirmPassword = ref('');
let passwordChangeError = ref('')

onMounted(() => {
  const storedUser = localStorage.getItem('loggedUser');
  if (storedUser) {
    loggedUser.value = JSON.parse(storedUser);
    console.log("Logged user:", loggedUser.value);
    if (currentTab.value === 'profile') {
      loadProfileData();
    }
  } else {
    console.error("No logged user found in localStorage.");
  }
});

const loadProfileData = async () => {
  if (!loggedUser.value) {
    console.error("Logged user is null.");
    return;
  }
  try {
    const response = await axios.get(`http://localhost:8080/chocolate-factory/rest/users/${loggedUser.value.id}`);
    console.log("Profile data loaded:", response.data);
    
    profile.value = {
      username: response.data.username,
      password: response.data.password,
      firstName: response.data.name,
      lastName: response.data.surname,
      gender: response.data.gender,
      birthday: response.data.dateOfBirth
    };

  } catch (error) {
    console.error("Failed to load profile data:", error);
  }
};

const saveProfile = async () => {
  if (!loggedUser.value) {
    console.error("Logged user is null.");
    return;
  }
  try {
    axios.put(`http://localhost:8080/chocolate-factory/rest/users/${loggedUser.value.id}`, profile.value);
    console.log("Profile data saved successfully.");
    window.alert("Profile data saved successfully.");
  } catch (error) {
    console.error("Failed to save profile data:", error);
  }
};


const changePassword = async () => {
  if (!loggedUser.value) {
    console.error("Logged user is null.");
    return;
  }
  if (newPassword.value !== confirmPassword.value) {
    console.error("New passwords do not match.");
    return;
  }

  try {
    const response = await axios.put(`http://localhost:8080/chocolate-factory/rest/users/changePassword/${loggedUser.value.id}`, {
      currentPassword: currentPassword.value,
      newPassword: newPassword.value
    });
    console.log("Password changed successfully.", response.data);
    window.alert("Password changed successfully.");
    changePasswordModalVisible.value = false; 
  } catch (error) {
    if (error.response && error.response.status === 401) {
      passwordChangeError.value = "Current password is incorrect.";
    } else {
      console.error("Failed to change password:", error);
    }
  }
};


</script>

<style>
.my-account {
  width: 800px;
  margin: 50px auto;
}

.tabs {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.tabs button {
  padding: 10px 20px;
  cursor: pointer;
  border: none;
  border-radius: 5px;
  background-color: lightgray;
  flex: 1;
  text-align: center;
  font-weight: 600;
  font-family: "Poppins", sans-serif;
}



.tabs button.active {
  background-color: #523F31;
  color: white;
}

.tab-content {
  padding: 20px;
}

form label {
  display: block;
  margin-bottom: 15px;
}

form label input,
form label select {
  display: block;
  margin-top: 5px;
  padding: 10px;
  width: calc(100% - 22px);
  border: 1px solid #ccc;
  border-radius: 5px;
}

button[type="submit"] {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #523F31;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: 600;
  font-family: "Poppins", sans-serif;
}

button[type="button"] {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #523F31;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: 600;
  font-family: "Poppins", sans-serif;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

table, th, td {
  border: 1px solid black;
}

th, td {
  padding: 10px;
  text-align: left;
}

.modal {
  display: block;
  position: fixed;
  z-index: 1000;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0,0,0,0.5);
}

.modal-content {
  background-color: #fefefe;
  margin: 15% auto;
  padding: 20px;
  border: 1px solid #888;
  width: 50%; 
  max-width: 400px; 
}

.close {
  color: #aaaaaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: #000;
  text-decoration: none;
  cursor: pointer;
}

.modal-content form label {
  display: block;
  margin-bottom: 15px;
}

.modal-content form label input {
  display: block;
  margin-top: 5px;
  padding: 8px; 
  width: calc(100% - 16px); 
  border: 1px solid #ccc;
  border-radius: 5px;
}

.modal-content form button[type="submit"] {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #523F31;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: 600;
  font-family: "Poppins", sans-serif;
}

.success-message {
  margin-top: 10px;
  padding: 10px;
  background-color: #4CAF50;
  color: white;
  border-radius: 5px;
  text-align: center;
}
</style>
