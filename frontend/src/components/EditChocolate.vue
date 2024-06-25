<template>
  <div class="edit-chocolate-container">
    <h1>Edit Chocolate</h1>
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
          <option v-for="factory in factories" :key="factory.id" :value="factory.id">
            {{ factory.name }}
          </option>
        </select>
      </div>
      <div class="form-group">
        <label for="image">Image URL: </label>
        <input type="url" v-model="chocolate.image" @input="preview = chocolate.image" id="image" :style="{ backgroundColor: imageFill ? 'white' : 'red' }"  />
      </div>
      <div class="form-group">
        <img v-if="preview" :src="preview" alt="Chocolate Image Preview" />
      </div>
      <button type="submit">Save Changes</button>
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
      preview: null
    };
  },
  mounted() {
    this.loadFactories();
    this.loadChocolate();
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
    loadChocolate() {
      const chocolateId = this.$route.params.chocolateId;
      axios.get(`http://localhost:8080/chocolate-factory/rest/chocolates/${chocolateId}`)
        .then(response => {
          this.chocolate = response.data;
          const factoryId = this.chocolate.factory;
          axios.get(`http://localhost:8080/chocolate-factory/rest/factories/${factoryId}`)
            .then(response => {
              console.log('Factory details:', response.data);
              this.chocolate.factory = response.data.id;
            })
            .catch(error => {
              console.error('Error fetching factory:', error);
            });
        })
        .catch(error => {
          console.error('Error fetching chocolate:', error);
        });
    },
    submitForm() {
      this.checkFields();
      if (!this.nameFill || !this.priceFill || !this.kindFill || !this.typeFill || !this.weightFill || !this.descriptionFill || !this.factoryFill || !this.imageFill) {
        return;
      }
      this.factories.forEach((f) => {
        if (f.name === this.chocolate.factory) {
          this.chocolate.factory = f.id;
        }
      });
      axios.put(`http://localhost:8080/chocolate-factory/rest/chocolates/${this.chocolate.id}`, this.chocolate)
        .then(response => {
          const factoryId = this.chocolate.factory;
          this.$router.push({ name: 'chocolates', params: { factoryId } });
        })
        .catch(error => {
          console.error('Error updating chocolate:', error);
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
.edit-chocolate-container {
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
  margin-bottom: 20px;
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
  transition: background-color 0.3s ease;
  font-family: "Poppins", sans-serif;
  font-weight: 500;
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
