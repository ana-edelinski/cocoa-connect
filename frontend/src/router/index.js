import { createRouter, createWebHashHistory } from 'vue-router';
import HelloWorld from '../components/HelloWorld.vue';
import AddChocolate from '../components/AddChocolate.vue';
import Factory from '../components/Factory.vue';
import Chocolate from '../components/Chocolate.vue';
import EditChocolate from '../components/EditChocolate.vue';
import CreateFactory from '../components/CreateFactory.vue';
import Registration from '../components/Registration.vue';
import SignIn from '../components/SignIn.vue';
import MyAccount from '../components/MyAccount.vue'
const routes = [
  {
    path: "/",
    name: "home",
    component: Factory,
  },
  {
    path: "/add-chocolate",
    name: "addChocolate",
    component: AddChocolate
  },
  {
    path: "/edit-chocolate/:chocolateId",
    name: "editChocolate",
    component: EditChocolate
  },
  {
    path: "/chocolates/:factoryId",
    name: "chocolates",
    component: Chocolate
  },
  
  {
    path: "/create-factory",
    name: "createFactory",
    component: CreateFactory
  },
  {
    path: "/my-account",
    name: "myAccount",
    component: MyAccount
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
  {
    path: "/registration",
    name: "registration",
    component: Registration
  },
  {
    path: "/signIn",
    name: "signIn",
    component: SignIn
  }
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
