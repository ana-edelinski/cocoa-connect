<template>
    <div class="user-list-container">
      <h1>Registered Users</h1>
      <table>
        <thead>
          <tr>
            <th>Username</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Gender</th>
            <th>Date of Birth</th>
            <th>Role</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.username">
            <td>{{ user.username }}</td>
            <td>{{ user.name }}</td>
            <td>{{ user.surname }}</td>
            <td>{{ user.gender }}</td>
            <td>{{ formatDate(user.dateOfBirth) }}</td>
            <td>{{ user.role }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    name: 'AdminUserList',
    data() {
      return {
        users: []
      };
    },
    created() {
      this.fetchUsers();
    },
    methods: {
      fetchUsers() {
        axios.get('http://localhost:8080/chocolate-factory/rest/users/')
          .then(response => {
            this.users = response.data;
          })
          .catch(error => {
            console.error('Error fetching users:', error);
          });
      },
      formatDate(date) {
        const d = new Date(date);
        return d.toLocaleDateString();
      }
    }
  };
  </script>
  
  <style scoped>
  .user-list-container {
    max-width: 800px;
    margin: 50px auto;
    padding: 20px 0; /* Remove padding for left and right */
    overflow-x: auto; /* Ensure horizontal scroll if table is wider */
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
  }
  tr:hover {
    background-color: #f1f1f1;
  }
  tbody tr:last-child td {
    border-bottom: 2px solid #523F31; /* Thicker border for the last row */
  }
  </style>
  