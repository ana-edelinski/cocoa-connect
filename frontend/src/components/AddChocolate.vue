<template>
  <div class="add-chocolate-container">
    <h1>Add New Chocolate</h1>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="name">Name: </label>
        <input type="text" :style="{ backgroundColor: nameFill ? 'white' : 'red' }" v-model="chocolate.name" id="name" required />
      </div>
      <div class="form-group">
        <label for="price">Price: </label>
        <input type="number" :style="{ backgroundColor: priceFill ? 'white' : 'red' }" v-model="chocolate.price" id="price" required />
      </div>
      <div class="form-group">
        <label for="kind">Kind: </label>
        <select :style="{ backgroundColor: kindFill ? 'white' : 'red' }" v-model="chocolate.kind" id="kind" required>
          <option value="REGULAR">Regular</option>
          <option value="COOKING">Cooking</option>
          <option value="DRINKING">Drinking</option>
        </select>
      </div>
      <div class="form-group">
        <label for="type">Type: </label>
        <select :style="{ backgroundColor: typeFill ? 'white' : 'red' }" v-model="chocolate.type" id="type" required>
          <option value="MILK">Milk</option>
          <option value="DARK">Dark</option>
          <option value="WHITE">White</option>
        </select>
      </div>
      <div class="form-group">
        <label for="weight">Weight [grams]:</label>
        <input type="number" :style="{ backgroundColor: weightFill ? 'white' : 'red' }" v-model="chocolate.weight" id="weight" required />
      </div>
      <div class="form-group">
        <label for="description">Description: </label>
        <textarea :style="{ backgroundColor: descriptionFill ? 'white' : 'red' }" v-model="chocolate.description" id="description" required></textarea>
      </div>
      <div class="form-group">
        <label for="factory">Factory: </label>
        <select v-model="chocolate.factory" id="factory" required>
          <option :style="{ backgroundColor: factoryFill ? 'white' : 'red' }" v-for="factory in factories" :key="factory.id" :value="factory.name">
            {{ factory.name }}
          </option>
        </select>
      </div>
      <div class="form-group file-input-group">
        <label for="image">Image: </label>
        <input type="file" @change="handleImageUpload" id="image" required/>
        <button type="button" @click="previewImage">Preview</button>
      </div>
      <div class="form-group">
        <img v-if="preview" :src="preview" alt="Chocolate Image Preview" />
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
        image: null,
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
      factories: [],
      preview: null
    };
  },
  mounted() {
    this.loadFactories();
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
    handleImageUpload(event) {
      const file = event.target.files[0];
      this.chocolate.image = file.name;
      this.preview = URL.createObjectURL(file);
    },
    previewImage() {
      // TODO
    },
    submitForm() {
      // TODO
      console.log('Form submitted:', this.chocolate);
    },
    create() {
      this.checkFields();
      if (!this.nameFill || !this.priceFill || !this.kindFill || !this.typeFill || !this.weightFill || !this.descriptionFill || !this.factoryFill) {
        return;
      }
      this.factories.forEach((f) => {
        if (f.name === this.chocolate.factory) {
          this.chocolate.factory = f.id;
        }
      });
      axios.post('http://localhost:8080/backend/rest/chocolates/', this.chocolate)
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
    }
  }
};
</script>

<style scoped>
.add-chocolate-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  border: 2px solid #43392F;
  border-radius: 10px;
}
h1 {
  text-align: center;
  color: #43392F;
}
.form-group {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}
label {
  flex: 0 0 150px;
  margin-right: 10px;
}
input, select, textarea {
  flex: 1;
  padding: 8px;
  box-sizing: border-box;
}
button {
  background-color: #43392F;
  color: white;
  padding: 10px 20px;
  border: none;
  cursor: pointer;
}
button[type="submit"] {
  display: block;
  width: 100%;
  margin-top: 20px;
}
img {
  display: block;
  margin-top: 10px;
  max-width: 100%;
}
.file-input-group {
  display: flex;
  align-items: center;
}
.file-input-group input[type="file"] {
  flex: 1;
}
.file-input-group button {
  margin-left: 10px;
}
</style>
