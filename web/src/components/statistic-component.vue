<template>
  <div>
    <div class="tip">
      <div><b>  Tips：</b></div>
      <div>1. All statistic data are real with 1 hours delay. </div>
      <div>2. Whenever a document is voted, everyone will get a notification. </div>
      <div>3. You can extend the left menu after you login and add new categories. </div>
      <div>4. The document tree can be expanded with unlimited depth. </div>
    </div>

    <a-row :gutter="16">
        <a-col :span="8">
          <a-card title="Total View Count">
            <a-statistic :value="statistic.viewCount">
              <template #prefix>
                <UserOutlined />
              </template>
            </a-statistic>
          </a-card>
        </a-col>
        <a-col :span="8">
          <a-card title="Total Vote Count">
            <a-statistic :value="statistic.voteCount">
              <template #prefix>
                <like-outlined />
              </template>
            </a-statistic>
          </a-card>
        </a-col>

        <a-col :span="8">
          <a-card title="Vote Ratio">
            <a-statistic :value="statistic.voteCount / statistic.viewCount * 100"
                         :precision="2"
                         suffix="%"
                         :value-style="{ color: '#cf1322' }">
              <template #prefix>
                <like-outlined />
              </template>
            </a-statistic>
          </a-card>
        </a-col>
      </a-row>
    <br>
    <a-row :gutter="16">
      <a-col :span="12">
        <a-card>
          <a-row>
            <a-col :span="12">
              <a-statistic title="View Count Today" :value="statistic.todayViewCount" style="margin-right: 50px">
                <template #prefix>
                  <UserOutlined />
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="12">
              <a-statistic title="Vote Count Today" :value="statistic.todayVoteCount">
                <template #prefix>
                  <like-outlined />
                </template>
              </a-statistic>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card>
          <a-row>
            <a-col :span="12">
              <a-statistic
                  title="View Count Increment for Today"
                  :value="statistic.todayViewIncrease"
                  :value-style="{ color: '#0000ff' }"
              >
                <template #suffix>
                  <UserOutlined />
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="12">
              <a-statistic
                  title="View Count Increment Prediction for Today"
                  :value="statistic.todayViewIncreaseRateAbs"
                  :precision="2"
                  suffix="%"
                  class="demo-class"
                  :value-style="statistic.todayViewIncreaseRate < 0 ? { color: '#3f8600' } : { color: '#cf1322' }"
              >
                <template #prefix>
                  <arrow-down-outlined v-if="statistic.todayViewIncreaseRate < 0"/>
                  <arrow-up-outlined v-if="statistic.todayViewIncreaseRate >= 0"/>
                </template>
              </a-statistic>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>
  </div>

  <a-row>
    <a-col :span="24" id="main-col">
      <div id="main" style="width: 100%;height:300px;"></div>
    </a-col>
  </a-row>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue'
import axios from 'axios';

//declare let echarts: any;
declare let echarts: any;
export default defineComponent({
  name: 'statistic-component',
  setup () {
    const statistic = ref();
    statistic.value = {};

    const getStatistic = () => {
      axios.get('/ebook-snapshot/get-statistic').then((response) => {
        const data = response.data;
        if (data.success) {
          const statisticResp = data.content;
          statistic.value.viewCount = statisticResp[1].viewCount;
          statistic.value.voteCount = statisticResp[1].voteCount;
          statistic.value.todayViewCount = statisticResp[1].viewIncrease;
          statistic.value.todayVoteCount = statisticResp[1].voteIncrease;

          // Percentage of today's seconds elapsed.
          const now = new Date();
          const nowRate = (now.getHours() * 60 + now.getMinutes()) / (60 * 24);
          // console.log(nowRate)
          statistic.value.todayViewIncrease = parseInt(String(statisticResp[1].viewIncrease / nowRate));
          // todayViewIncreaseRate: Today's Increment Ratio.
          statistic.value.todayViewIncreaseRate = (statistic.value.todayViewIncrease - statisticResp[0].viewIncrease) / statisticResp[0].viewIncrease * 100;
          statistic.value.todayViewIncreaseRateAbs = Math.abs(statistic.value.todayViewIncreaseRate);
          statistic.value.voteRatio = statistic.value.voteCount / statistic.value.viewCount * 100
        }
      });
    };

    const init30DayEcharts = (list: any) => {
      // Production Env Error: Failed to load chart when switching pages.
      // Solution: clear id=main and re-init
      const mainDom = document.getElementById('main-col');
      if (mainDom) {
        mainDom.innerHTML = '<div id="main" style="width: 100%;height:300px;"></div>';
      }
      // Init Echart
      const myChart = echarts.init(document.getElementById('main'));

      const xAxis = [];
      const seriesView = [];
      const seriesVote = [];
      for (let i = 0; i < list.length; i++) {
        const record = list[i];
        xAxis.push(record.date);
        seriesView.push(record.viewIncrease);
        seriesVote.push(record.voteIncrease);
      }

      // Setup Echart Meta
      const option = {
        title: {
          text: '30 Days Tendency'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['Total View Count', 'Total Vote Count']
        },
        grid: {
          left: '1%',
          right: '3%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: xAxis
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: 'Total View Count',
            type: 'line',
            data: seriesView,
            smooth: true
          },
          {
            name: 'Total Vote Count',
            type: 'line',
            data: seriesVote,
            smooth: true
          }
        ]
      };

      myChart.setOption(option);
    };

    const get30DayStatistic = () => {
      axios.get('/ebook-snapshot/get-30-statistic').then((response) => {
        const data = response.data;
        if (data.success) {
          const statisticList = data.content;

          init30DayEcharts(statisticList)
        }
      });
    };

    onMounted(() => {
      getStatistic();
      get30DayStatistic();
    });

    return {
      statistic
    }
  }
});
</script>
<style scoped>
.tip {
  padding: 10px 5px;
  margin-bottom: 20px;
  color: red;
  border: 1px solid transparent;
  background: linear-gradient(white,white) padding-box,repeating-linear-gradient(-45deg, black 0, black 25%, white 0, white 50%) 0/.6em .6em;
  animation:ants 12s linear infinite;
}
</style>
