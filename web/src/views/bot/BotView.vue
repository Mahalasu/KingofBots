<template>
  <ContentField>
    <div class="row">
      <div class="col-3">
        <div class="card mb-3">
          <div class="card-body">
            <img
              class="w-100"
              :src="'data:image/svg+xml;utf8,' + $store.state.user.avatar"
              alt=""
            />
          </div>
        </div>
        <div class="card">
          <div class="card-header">User Info</div>
          <ul class="list-group list-group-flush">
            <li class="list-group-item">
              Username: {{ $store.state.user.username }}
            </li>
            <li class="list-group-item"># of Bots: {{ bots.length }}</li>
            <li class="list-group-item">
              Max Marks: {{ $store.state.user.marks }}
            </li>
          </ul>
        </div>
      </div>
      <div class="col-9">
        <div class="card">
          <div class="card-header" style="display: flex; align-items: center">
            <span class="fs-5 me-auto">My Bots</span>
            <button
              type="button"
              class="btn btn-primary"
              data-bs-toggle="modal"
              data-bs-target="#createNewBot"
            >
              Create New Bot
            </button>
            <CreateModal @created="getList" />
          </div>
          <table class="table table-striped table-hover mb-0">
            <thead>
              <tr>
                <th>Name</th>
                <th>Created Time</th>
                <th>Last Modify Time</th>
                <th>Operation</th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="bot in bots"
                :key="bot.id"
                style="vertical-align: middle"
              >
                <td>{{ bot.name }}</td>
                <td>{{ timeShift(bot.createTime) }}</td>
                <td>{{ timeShift(bot.lastModify) }}</td>
                <td>
                  <button
                    type="button"
                    class="btn btn-primary"
                    data-bs-toggle="modal"
                    data-bs-target="#editBot"
                    @click="setId(bot.id)"
                  >
                    Update
                  </button>
                  <button
                    type="button"
                    class="btn btn-danger mx-1 my-1"
                    @click="deleteBot(bot)"
                  >
                    Delete
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
          <EditModal v-model:id="botId" @gotten="setId()" @edited="getList" />
        </div>
      </div>
    </div>
  </ContentField>
</template>

<script>
import CreateModal from '@/components/CreateModal'
import EditModal from '@/components/EditModal'
import ContentField from '@/components/ContentField'
import { ref } from 'vue'
import $ from 'jquery'
import { useStore } from 'vuex'
import moment from 'moment'
import { domainNameProd } from '@/global'

export default {
  components: {
    CreateModal,
    EditModal,
    ContentField,
  },
  setup() {
    const store = useStore()
    let bots = ref([])
    let botId = ref()

    const timeShift = (originalDateStr) =>
      moment.utc(originalDateStr).local().format('YYYY-MM-DD HH:mm:ss')

    const getList = () => {
      $.ajax({
        url: `${domainNameProd}/api/user/bot/list`,
        type: 'get',
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        success(resp) {
          bots.value = resp
        },
      })
    }

    const deleteBot = (bot) => {
      $.ajax({
        url: `${domainNameProd}/api/user/bot/delete`,
        type: 'post',
        data: {
          id: bot.id,
        },
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        success() {
          getList()
        },
      })
    }

    const setId = (id = null) => {
      botId.value = id
    }

    getList()

    return {
      bots,
      getList,
      deleteBot,
      setId,
      botId,
      timeShift,
    }
  },
}
</script>

<style scoped></style>
