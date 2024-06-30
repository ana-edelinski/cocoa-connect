<template>
  <div class="signin-container">
    <div class="signin-form">
      <h1>Sign In</h1>
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="username">Username:</label>
          <input type="text" v-model="form.username" id="username" :style="{ backgroundColor: usernameFill ? 'white' : 'red' }" required>
        </div>
        <div class="form-group">
          <label for="password">Password:</label>
          <input type="password" v-model="form.password" id="password" :style="{ backgroundColor: passwordFill ? 'white' : 'red' }" required>
        </div>
        <button type="submit">Sign In</button>
      </form>
      <p class="register-link">
        No account yet? <router-link to="/registration">CREATE AN ACCOUNT</router-link>
      </p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'SignIn',
  data() {
    return {
      form: {
        username: '',
        password: ''
      },
      usernameFill: true,
      passwordFill: true
    };
  },
  methods: {
    handleSubmit() {
      this.checkFields();
      if (!this.usernameFill || !this.passwordFill) {
        return;
      }

      axios.post('http://localhost:8080/chocolate-factory/rest/users/login', this.form)
        .then(response => {
            alert('Login successfull!');
           
            let loggedUser = JSON.stringify(response.data);
            localStorage.setItem('loggedUser', loggedUser);

            this.$router.push('/');
        })
        .catch(error => {
            console.error('Error registering:', error);
            alert('Login failed!');
        });      
    },
    checkFields() {
      this.usernameFill = this.form.username !== '';
      this.passwordFill = this.form.password !== '';
    }
  }
};
</script>

<style scoped>
.signin-container {
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
input {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 16px;
}
input:focus {
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

.register-link {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
}

.register-link router-link {
  color: #523F31;
  text-decoration: underline;
  cursor: pointer;
}
</style>
