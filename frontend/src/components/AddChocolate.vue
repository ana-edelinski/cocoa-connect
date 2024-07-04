<template>
  <div class="add-chocolate-container">
    <h1>Add New Chocolate</h1>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="name">Name: </label>
        <input type="text" :style="{ backgroundColor: nameFill ? 'white' : 'red' }" v-model="chocolate.name" id="name"  />
      </div>
      <div class="form-group">
        <label for="price">Price: </label>
        <input type="number" :style="{ backgroundColor: priceFill ? 'white' : 'red' }" v-model="chocolate.price" id="price"  />
      </div>
      <div class="form-group">
        <label for="kind">Kind: </label>
        <select :style="{ backgroundColor: kindFill ? 'white' : 'red' }" v-model="chocolate.kind" id="kind" >
          <option value="REGULAR">Regular</option>
          <option value="COOKING">Cooking</option>
          <option value="DRINKING">Drinking</option>
        </select>
      </div>
      <div class="form-group">
        <label for="type">Type: </label>
        <select :style="{ backgroundColor: typeFill ? 'white' : 'red' }" v-model="chocolate.type" id="type" >
          <option value="MILK">Milk</option>
          <option value="DARK">Dark</option>
          <option value="WHITE">White</option>
        </select>
      </div>
      <div class="form-group">
        <label for="weight">Weight [grams]:</label>
        <input type="number" :style="{ backgroundColor: weightFill ? 'white' : 'red' }" v-model="chocolate.weight" id="weight"  />
      </div>
      <div class="form-group">
        <label for="description">Description: </label>
        <textarea :style="{ backgroundColor: descriptionFill ? 'white' : 'red' }" v-model="chocolate.description" id="description" ></textarea>
      </div>
      <div class="form-group">
        <label for="factory">Factory: </label>
        <select v-model="chocolate.factory" id="factory" :style="{ backgroundColor: factoryFill ? 'white' : 'red' }" >
          <option v-for="factory in factories" :key="factory.id" :value="factory.name">
            {{ factory.name }}
          </option>
        </select>
      </div>
      <div class="form-group">
        <label for="image">Image URL: </label>
        <input type="text" v-model="chocolate.image" id="image" :style="{ backgroundColor: factoryFill ? 'white' : 'red' }" />
      </div>
      <div class="form-group">
        <img v-if="chocolate.image" :src="chocolate.image" alt="Chocolate Image Preview" />
      </div>
      <button type="submit" @click="create">Add Chocolate</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios';
import { useRouter } from 'vue-router';
import { onMounted, ref } from 'vue';

export default {
  data() {
    return {
      chocolate: {
        name: '',
        price: '',
        type: '',
        kind: '',
        weight: '',
        description: '',
        image: '',
        quantity: 0,
        factory: ''
      },
      nameFill: true,
      priceFill: true,
      kindFill: true,
      typeFill: true,
      weightFill: true,
      descriptionFill: true,
      factoryFill: true,
      imageFill: true,
      factories: [],
      loggedUser: JSON.parse(localStorage.getItem('loggedUser')),
      preview: null
    };
  },
  
  mounted() {
    this.loadFactories();
  },
  methods: {
    loadFactories() {
    if (this.loggedUser && this.loggedUser.id) {
      axios.get(`http://localhost:8080/chocolate-factory/rest/factories/manager/${this.loggedUser.id}`)
        .then(response => {
          if (Array.isArray(response.data)) {
            this.factories = response.data;
          } else if (response.data && typeof response.data === 'object') {
            this.factories = [response.data];
          } else {
            console.error("Unexpected response data format:", response.data);
          }
        })
        .catch(error => {
          console.error('Error fetching factories:', error);
        });
    } else {
      console.error('No logged user found.');
    }
  },

    submitForm() {
      // TODO
      console.log('Form submitted:', this.chocolate);
    },
    create() {
      this.checkFields();
      if (!this.nameFill || !this.priceFill || !this.kindFill || !this.typeFill || !this.weightFill || !this.descriptionFill || !this.factoryFill ||!this.imageFill) {
        return;
      }
      this.factories.forEach((f) => {
        if (f.name === this.chocolate.factory) {
          this.chocolate.factory = f.id;
        }
      });
      axios.post('http://localhost:8080/chocolate-factory/rest/chocolates/', this.chocolate)
        .then(response => {
          const factoryId = this.chocolate.factory;
          this.$router.push({ name: 'chocolates', params: { factoryId } });
        })
        .catch(error => {
          console.error('Error fetching factories:', error);
        });
    },
    checkFields() {
      this.nameFill = this.chocolate.name !== '';
      this.priceFill = this.chocolate.price > 0;
      this.kindFill = this.chocolate.kind !== '';
      this.typeFill = this.chocolate.type !== '';
      this.weightFill = this.chocolate.weight > 0;
      this.descriptionFill = this.chocolate.description !== '';
      this.factoryFill = this.chocolate.factory !== '';
      this.imageFill = this.chocolate.image !== '';
    }
  }
};
</script>

<style scoped>
.add-chocolate-container {
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
input, select, textarea {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 16px;
}
input:focus, select:focus, textarea:focus {
  border-color: #523F31;
  outline: none;
}
button {
  background-color: #523F31;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
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
img {
  display: block;
  margin-top: 10px;
  max-width: 100%;
  border: 1px solid #ddd;
  border-radius: 5px;
}
</style>
