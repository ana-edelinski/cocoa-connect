<template>
  <div class="registration-container">
    <div class="registration-form">
      <h1>Registration</h1>
      <form @submit.prevent="handleSubmit">
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
        <button type="submit">Register</button>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { useRouter } from 'vue-router';

export default {
  name: 'Registration',
  data() {
    return {
      form: {
        username: '',
        password: '',
        confirmPassword: '',
        name: '',
        surname: '',
        gender: '',
        dateOfBirth: ''
      },
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
    handleSubmit() {
      this.checkFields();
      if (!this.usernameFill || !this.passwordFill || !this.confirmPasswordFill || !this.firstNameFill || !this.lastNameFill || !this.genderFill || !this.birthDateFill) {
        console.log('Please fill in all fields correctly.');
        return;
      }
      if (this.form.password !== this.form.confirmPassword) {
        alert('Passwords do not match.');
        return;
      }

      console.log('Submitting form:', this.form);

      axios.post('http://localhost:8080/chocolate-factory/rest/users/register', this.form)
        .then(response => {
          console.log('Form successfully submitted!', response.data);
          this.$router.push('/signIn');
        })
        .catch(error => {
          alert('Username already existst:');
        });
    },
    checkFields() {
      this.usernameFill = this.form.username !== '';
      this.passwordFill = this.form.password !== '';
      this.confirmPasswordFill = this.form.confirmPassword !== '';
      this.firstNameFill = this.form.name !== '';
      this.lastNameFill = this.form.surname !== '';
      this.genderFill = this.form.gender !== '';
      this.birthDateFill = this.form.dateOfBirth !== '';
    }
  }
};
</script>

<style scoped>
.registration-container {
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
  font-size: 18px;
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
