<template>
    <div class="my-account">
      <div class="tabs">
        <button :class="{ active: currentTab === 'profile' }" @click="currentTab = 'profile'">MY PROFILE</button>
        <button :class="{ active: currentTab === 'orders' }" @click="currentTab = 'orders'">MY ORDERS</button>
        <button :class="{ active: currentTab === 'cancelRequests' }" @click="currentTab = 'cancelRequests'">CANCEL REQUESTS</button>
      </div>
      <div class="tab-content" v-if="currentTab === 'profile'">
        <h2>Profile Information</h2>
        <form @submit.prevent="saveProfile">
          <label>
            Username:
            <input type="text" v-model="profile.username" required>
          </label>
          <label>
            Password:
            <input type="password" v-model="profile.password" required>
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
              <option value="male">Male</option>
              <option value="female">Female</option>
              <option value="other">Other</option>
            </select>
          </label>
          <label>
            Birthday:
            <input type="date" v-model="profile.birthday" required>
          </label>
          <button type="submit">Save</button>
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
  </template>
  
  <script>
  export default {
    data() {
      return {
        currentTab: 'profile',
        profile: {
          username: '',
          password: '',
          firstName: '',
          lastName: '',
          gender: 'male',
          birthday: ''
        },
        orders: [
          { id: 1, date: '2023-05-15', status: 'Shipped' },
          { id: 2, date: '2023-06-10', status: 'Pending' }
        ],
        cancelRequests: [
          { id: 1, orderId: 1, date: '2023-05-20', status: 'Processed' },
          { id: 2, orderId: 2, date: '2023-06-12', status: 'Pending' }
        ]
      };
    },
    methods: {
      saveProfile() {
        // Add logic to save profile information
        alert('Profile saved!');
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
    font-family: "Poppins", sans-serif
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
    font-family: "Poppins", sans-serif
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
  </style>
  