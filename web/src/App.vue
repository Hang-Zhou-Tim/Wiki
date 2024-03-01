<template>
  <a-layout id="components-layout-demo-top-side-2">
    <HeaderComponent></HeaderComponent>
    <router-view/>
    <FooterComponent></FooterComponent>
  </a-layout>
</template>


<script>
import {defineComponent} from 'vue';
import HeaderComponent from '@/components/header-component.vue';
import FooterComponent from '@/components/footer-component.vue';

export default defineComponent({
    name: "app",
    components: {
      HeaderComponent,
      FooterComponent,
    }
  })
</script>

<style>

</style>
