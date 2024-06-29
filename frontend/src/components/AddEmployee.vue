<template>
    <div class="employee-registration-container">
      <div class="employee-registration-form">
        <h1>Add Employee</h1>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label for="factory">Factory: </label>
            <select v-model="form.factory" id="factory" :style="{ backgroundColor: factoryFill ? 'white' : 'red' }" >
              <option v-for="factory in factories" :key="factory.id" :value="factory.name">
                {{ factory.name }}
              </option>
            </select>
          </div>  
          <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" v-model="form.username" id="username" :style="{ backgroundColor: usernameFill ? 'white' : 'red' }" >
          </div>
          <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" v-model="form.password" id="password" :style="{ backgroundColor: passwordFill ? 'white' : 'red' }" >
          </div>
          <div class="form-group">
            <label for="confirm_password">Confirm Password:</label>
            <input type="password" v-model="form.confirmPassword" id="confirm_password" :style="{ backgroundColor: confirmPasswordFill ? 'white' : 'red' }" >
          </div>
          <div class="form-group">
            <label for="first_name">First Name:</label>
            <input type="text" v-model="form.name" id="first_name" :style="{ backgroundColor: firstNameFill ? 'white' : 'red' }" >
          </div>
          <div class="form-group">
            <label for="last_name">Last Name:</label>
            <input type="text" v-model="form.surname" id="last_name" :style="{ backgroundColor: lastNameFill ? 'white' : 'red' }" >
          </div>
          <div class="form-group">
            <label for="gender">Gender:</label>
            <select v-model="form.gender" id="gender" :style="{ backgroundColor: genderFill ? 'white' : 'red' }" >
              <option value="MALE">Male</option>
              <option value="FEMALE">Female</option>
            </select>
          </div>
          <div class="form-group">
            <label for="birth_date">Birth Date:</label>
            <input type="date" v-model="form.dateOfBirth" id="birth_date" :style="{ backgroundColor: birthDateFill ? 'white' : 'red' }" >
          </div>
          <button type="submit">Add Employee</button>
        </form>
      </div>
    </div>
  </template>
  
  <script>
import axios from 'axios';
import { useRouter } from 'vue-router';

export default {
  name: 'AddEmployee',
  data() {
    return {
      form: {
        factory: '',
        username: '',
        password: '',
        confirmPassword: '',
        name: '',
        surname: '',
        gender: '',
        dateOfBirth: ''
      },
      factories: [],
      factoryFill: true,
      usernameFill: true,
      passwordFill: true,
      confirmPasswordFill: true,
      firstNameFill: true,
      lastNameFill: true,
      genderFill: true,
      birthDateFill: true,
    };
  },
  methods: {
    loadFactories() {
      axios.get('http://localhost:8080/backend/rest/factories/')
        .then(response => {
          this.factories = response.data;
          console.log(this.factories);
        })
        .catch(error => {
          console.error('Error fetching factories:', error);
        });
    },
    handleSubmit() {
      this.checkFields();
      if (!this.factoryFill || !this.usernameFill || !this.passwordFill || !this.confirmPasswordFill || !this.firstNameFill || !this.lastNameFill || !this.genderFill || !this.birthDateFill) {
        console.log('Please fill in all fields correctly.');
        return;
      }
      if (this.form.password !== this.form.confirmPassword) {
        alert('Passwords do not match.');
        return;
      }

      console.log('Submitting form:', this.form);

      axios.post('http://localhost:8080/backend/rest/users/employees', this.form)
        .then(response => {
          console.log('Employee successfully added!', response.data);
          this.$router.push('/'); 
        })
        .catch(error => {
          console.error('Error adding employee:', error);
        });
    },
    checkFields() {
      this.factoryFill = this.form.factory !== '';
      this.usernameFill = this.form.username !== '';
      this.passwordFill = this.form.password !== '';
      this.confirmPasswordFill = this.form.confirmPassword !== '';
      this.firstNameFill = this.form.name !== '';
      this.lastNameFill = this.form.surname !== '';
      this.genderFill = this.form.gender !== '';
      this.birthDateFill = this.form.dateOfBirth !== '';
    }
  },
  mounted() {
    this.loadFactories();
  }
};
</script>

  
  <style scoped>
  .employee-registration-container {
    max-width: 600px;
    margin: 50px auto;
    padding: 20px;
    border: 2px solid #523F31;
    border-radius: 10px;
    background-color: #f9f9f9;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  }
  h1 {
    text-align: center;
    color: #523F31;
  }
  .form-group {
    display: flex;
    flex-direction: column;
    margin-bottom: 15px;
  }
  label {
    margin-bottom: 5px;
    color: #333;
    text-align: left;
  }
  input, select {
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 5px;
    font-size: 16px;
  }
  input:focus, select:focus {
    border-color: #523F31;
    outline: none;
  }
  button {
    background-color: #523F31;
    color: white;
    padding: 15px 30px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 15px;
    font-weight: 500;
    transition: background-color 0.3s ease;
    font-family: "Poppins", sans-serif;
  }
  
  button[type="submit"] {
    display: block;
    width: auto;
    margin-top: 20px;
    margin: 20px auto 0;
  }
  
  button:hover {
    background-color: #796254;
  }
  </style>
  