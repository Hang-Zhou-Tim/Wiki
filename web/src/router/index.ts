import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import Home from '../views/home.vue'
import About from '../views/about.vue'
import Doc from '../views/doc.vue'
import AdminEbook from '../views/admin/admin-ebook.vue'
import AdminCategory from '../views/admin/admin-category.vue'
import AdminDoc from '../views/admin/admin-doc.vue'
import AdminUser from '../views/admin/admin-user.vue'
import store from "@/store";
import {Tool} from "@/util/tool";


const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/doc',
    name: 'Doc',
    component: Doc
  },
  {
    path: '/about',
    name: 'About',
    component: About
  },
  {
    path: '/admin/ebook',
    name: 'AdminEbook',
    component: AdminEbook,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/category',
    name: 'AdminCategory',
    component: AdminCategory,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/user',
    name: 'AdminUser',
    component: AdminUser,
    meta: {
      loginRequire: true
    }
  },
  {
    path: '/admin/doc',
    name: 'AdminDoc',
    component: AdminDoc,
    meta: {
      loginRequire: true
    }
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})
//Front-End Inteceptor for Unauthorised User.
router.beforeEach((to, from, next) => {
  // Check if the requested webbpage contains meta.loginRequire field.
  if (to.matched.some(function (item) {
    return item.meta.loginRequire
  })) {
    const loginUser = store.state.user;
    if (Tool.isEmpty(loginUser)) {
      alert("User is not logged in!");
      next('/');
    } else {
      next();
    }
  } else {
    next();
  }
});


export default router
