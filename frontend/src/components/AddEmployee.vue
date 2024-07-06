<template>
  <div class="employee-registration-container">
    <div class="employee-registration-form">
      <h1>Add Employee</h1>
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="factory">Factory: </label>
          <select v-model="form.factory" id="factory" :style="{ backgroundColor: factoryFill ? 'white' : 'red' }">
            <option v-for="factory in factories" :key="factory.id" :value="factory.id">{{ factory.name }}</option>
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

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();

const form = ref({
  factory: '',
  username: '',
  password: '',
  confirmPassword: '',
  name: '',
  surname: '',
  gender: '',
  dateOfBirth: '',
  role: 'EMPLOYEE',
  managerId: -1
});
const factories = ref([]);
const factoryFill = ref(true);
const usernameFill = ref(true);
const passwordFill = ref(true);
const confirmPasswordFill = ref(true);
const firstNameFill = ref(true);
const lastNameFill = ref(true);
const genderFill = ref(true);
const birthDateFill = ref(true);
const loggedUser = ref(null);

const loadFactories = () => {
  if (loggedUser.value && loggedUser.value.id) {
    console.log("Fetching factories for user ID:", loggedUser.value.id); 
    axios.get(`http://localhost:8080/chocolate-factory/rest/factories/manager/${loggedUser.value.id}`)
      .then(response => {
        console.log("Response data:", response.data);
        if (Array.isArray(response.data)) {
          factories.value = response.data; 
        } else if (response.data && typeof response.data === 'object') {
          factories.value = [response.data]; 
        } else {
          console.error("Unexpected response data format:", response.data);
        }
        console.log("Factories loaded:", factories.value);
      })
      .catch(error => {
        console.error('Error fetching factories:', error);
      });
  } else {
    console.error("Logged user ID is not set.");
  }
};



const handleSubmit = () => {
  checkFields();
  if (!factoryFill.value || !usernameFill.value || !passwordFill.value || !confirmPasswordFill.value || !firstNameFill.value || !lastNameFill.value || !genderFill.value || !birthDateFill.value) {
    console.log('Please fill in all fields correctly.');
    return;
  }
  if (form.value.password !== form.value.confirmPassword) {
    alert('Passwords do not match.');
    return;
  }

  form.value.role = 'EMPLOYEE';

  const storedUser = localStorage.getItem('loggedUser');
  const managerId = JSON.parse(storedUser).id;

  form.value.managerId = managerId;

  console.log('Submitting form:', form.value);

  axios.post('http://localhost:8080/chocolate-factory/rest/users/employees', form.value)
    .then(response => {
      console.log('Employee successfully added!', response.data);
      router.push('/'); 
    })
    .catch(error => {
     alert('Username already exists', error);
    });
};

const checkFields = () => {
  factoryFill.value = form.value.factory !== '';
  usernameFill.value = form.value.username !== '';
  passwordFill.value = form.value.password !== '';
  confirmPasswordFill.value = form.value.confirmPassword !== '';
  firstNameFill.value = form.value.name !== '';
  lastNameFill.value = form.value.surname !== '';
  genderFill.value = form.value.gender !== '';
  birthDateFill.value = form.value.dateOfBirth !== '';
};

onMounted(() => {
  const storedUser = localStorage.getItem('loggedUser');
  if (storedUser) {
    loggedUser.value = JSON.parse(storedUser);
    console.log("Logged user:", loggedUser.value);
    loadFactories();
  } else {
    console.error("No logged user found in localStorage.");
  }
});
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
