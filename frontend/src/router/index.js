import { createRouter, createWebHashHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import AddChocolateView from '../views/AddChocolateView.vue';
import AddChocolate from '../components/AddChocolate.vue';
import Factory from '../components/Factory.vue';

const routes = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/addChocolate",
    name: "addChocolate",
    component: AddChocolateView
  },
  {
    path: "/factories",
    name: "factories",
    component: AddChocolate
  },
  {
    path: "/about",
    name: "about",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/AboutView.vue"),
  },
  {
    path:"/factories",
    name:"factories",
    component: Factory,
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
