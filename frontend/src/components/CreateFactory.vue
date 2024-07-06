<template>
  <div class="create-factory-container">
    <h1>Create New Factory</h1>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="name">Name:</label>
        <input type="text" :style="{ backgroundColor: nameFill ? 'white' : 'red' }" v-model="factory.name" id="name" />
      </div>
      <div class="form-group">
        <label for="city">City:</label>
        <input type="text" :style="{ backgroundColor: cityFill ? 'white' : 'red' }" v-model="factory.city" id="city" />
      </div>
      <div class="form-group">
        <label for="country">Country:</label>
        <input type="text" :style="{ backgroundColor: countryFill ? 'white' : 'red' }" v-model="factory.country" id="country" />
      </div>
      <div class="form-group">
        <label for="workingHours">Working Hours:</label>
        <div class="time-inputs">
          <input type="time" :style="{ backgroundColor: workingHoursFill.start ? 'white' : 'red' }" v-model="factory.workingHours.startTime" id="workingHoursStart" />
          <input type="time" :style="{ backgroundColor: workingHoursFill.end ? 'white' : 'red' }" v-model="factory.workingHours.endTime" id="workingHoursEnd" />
        </div>
      </div>
      <div class="form-group">
        <label for="logo">Logo URL:</label>
        <input type="text" :style="{ backgroundColor: logoFill ? 'white' : 'red' }" v-model="factory.logo" id="logo" />
      </div>
      <div class="form-group">
        <img v-if="factory.logo" :src="factory.logo" alt="Factory Logo Preview" />
      </div>
      <div class="form-group">
        <label for="manager">Manager:</label>
        <select v-if="availableManagers.length" :style="{ backgroundColor: managerFill ? 'white' : 'red' }" v-model="factory.managerId" id="manager">
          <option v-for="manager in availableManagers" :key="manager.id" :value="manager.id">
            {{ manager.name }} {{ manager.surname }}
          </option>
        </select>
        <div v-else>
          <p>No available managers. Please <a href="#" @click.prevent="showRegisterManagerForm">register a new manager</a>.</p>
        </div>
      </div>
      <button type="submit">Create Factory</button>
    </form>

    <div v-if="showManagerForm" class="modal-overlay">
      <div class="modal-content">
        <h2>Register New Manager</h2>
        <div class="form-group">
          <label for="username">Username:</label>
          <input type="text" :style="{ backgroundColor: usernameFill ? 'white' : 'red' }" v-model="newManager.username" id="username" />
        </div>
        <div class="form-group">
          <label for="password">Password:</label>
          <input type="password" :style="{ backgroundColor: passwordFill ? 'white' : 'red' }" v-model="newManager.password" id="password" />
        </div>
        <div class="form-group">
          <label for="managerName">Name:</label>
          <input type="text" :style="{ backgroundColor: managerNameFill ? 'white' : 'red' }" v-model="newManager.name" id="managerName" />
        </div>
        <div class="form-group">
          <label for="surname">Surname:</label>
          <input type="text" :style="{ backgroundColor: surnameFill ? 'white' : 'red' }" v-model="newManager.surname" id="surname" />
        </div>
        <div class="form-group">
          <label for="gender">Gender:</label>
          <select v-model="newManager.gender" id="gender">
            <option value="MALE">Male</option>
            <option value="FEMALE">Female</option>
          </select>
        </div>
        <div class="form-group">
          <label for="dateOfBirth">Date of Birth:</label>
          <input type="date" v-model="newManager.dateOfBirth" id="dateOfBirth" />
        </div>
        <button @click.prevent="registerManager">Register Manager</button>
        <button @click.prevent="hideRegisterManagerForm">Close</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      factory: {
        name: '',
        city: '',
        country: '',
        workingHours: {
          startTime: '',
          endTime: ''
        },
        logo: '',
        managerId: '',
        averageRating: 0,
        comment: '',
        deleted: false,
        factoryStatus: 'OPENED'
      },
      newManager: {
        username: '',
        password: '',
        name: '',
        surname: '',
        gender: '',
        dateOfBirth: ''
      },
      nameFill: true,
      cityFill: true,
      countryFill: true,
      workingHoursFill: {
        start: true,
        end: true
      },
      logoFill: true,
      managerFill: true,
      usernameFill: true,
      passwordFill: true,
      managerNameFill: true,
      surnameFill: true,
      availableManagers: [],
      showManagerForm: false,
    };
  },
  mounted() {
    this.loadAvailableManagers();
  },
  methods: {
    loadAvailableManagers() {
      axios.get('http://localhost:8080/chocolate-factory/rest/users/available-managers')
        .then(response => {
          this.availableManagers = response.data;
        })
        .catch(error => {
          console.error('Error fetching available managers:', error);
        });
    },
    showRegisterManagerForm() {
      this.showManagerForm = true;
    },
    hideRegisterManagerForm() {
      this.showManagerForm = false;
    },
    registerManager() {
      this.checkManagerFields();
      if (!this.usernameFill || !this.managerNameFill || !this.surnameFill) return;

      axios.post('http://localhost:8080/chocolate-factory/rest/users/register-manager', this.newManager)
        .then(response => {
          alert('Registration successful');
          this.factory.managerId = response.data.id;
          this.showManagerForm = false;
          this.loadAvailableManagers();
        })
        .catch(error => {
          alert('Username already exists', error);
        });
    },
    submitForm() {
      this.checkFields();
      if (!this.nameFill || !this.cityFill || !this.countryFill || !this.isValidWorkingHours() || !this.logoFill || !this.managerFill) {
        console.log('Please fill in all fields correctly.');
        return;
      }

      axios.post('http://localhost:8080/chocolate-factory/rest/factories/', this.factory)
        .then(response => {
          this.$router.push({ name: 'factories' });
        })
        .catch(error => {
          console.error('Error creating factory:', error);
        });
    },
    checkFields() {
      this.nameFill = this.factory.name !== '';
      this.cityFill = this.factory.city !== '';
      this.countryFill = this.factory.country !== '';
      this.logoFill = this.factory.logo !== '';
      this.managerFill = this.factory.managerId !== '';
    },
    checkManagerFields() {
      this.usernameFill = this.newManager.username !== '';
      this.passwordFill = this.newManager.password !== '';
      this.managerNameFill = this.newManager.name !== '';
      this.surnameFill = this.newManager.surname !== '';
    },
    isValidWorkingHours() {
      const startTime = this.factory.workingHours.startTime;
      const endTime = this.factory.workingHours.endTime;
      this.workingHoursFill.start = !!startTime;
      this.workingHoursFill.end = !!endTime;
      return !!startTime && !!endTime;
    }
  }
};
</script>

<style scoped>
.create-factory-container {
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
  margin-bottom: 10px;
}

.form-group label {
  margin-bottom: 5px;
  color: #523F31;
  text-align: left;
}

.form-group input,
.form-group select {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.form-group input[type="time"] {
  padding: 4px;
}

.form-group img {
  max-width: 100%;
  height: auto;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-top: 10px;
}

.time-inputs {
  display: flex;
  justify-content: space-between;
}

.time-inputs input {
  width: 48%;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #523F31;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #634a38;
}

a {
  color: #523F31;
}

a:hover {
  text-decoration: underline;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  max-width: 400px;
  width: 100%;
}

.modal-content h2 {
  margin-top: 0;
  color: #523F31;
}

.modal-content .form-group {
  margin-bottom: 15px;
}

.modal-content button {
  margin-top: 10px;
}
</style>
