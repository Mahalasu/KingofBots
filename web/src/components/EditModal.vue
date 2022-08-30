<template>
  <div
    class="modal fade"
    id="editBot"
    tabindex="-1"
    data-bs-backdrop="static"
    aria-labelledby="editBotLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-xl">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="editBotLabel">Edit Bot</h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          <div class="mb-3">
            <label for="name" class="form-label">Name</label>
            <input
              v-model="botInfo.name"
              type="text"
              class="form-control"
              id="name"
              placeholder="Bot Name"
            />
          </div>
          <div class="mb-3">
            <label for="description" class="form-label">Description</label>
            <textarea
              v-model="botInfo.description"
              class="form-control"
              id="description"
              rows="3"
              placeholder="Your Bots' Description"
            ></textarea>
          </div>
          <VAceEditor
            v-model:value="botInfo.code"
            lang="java"
            theme="chrome"
            style="height: 300px"
          />
          <div
            class="alert alert-warning d-flex align-items-center my-3 mx-auto"
            role="alert"
          >
            <div>
              <span class="fw-bold">Warning:</span> Please follow the
              <a
                target="_blank"
                rel="noopener noreferrer"
                href="https://github.com/Mahalasu/KingofBots#bot-creation-instruction"
                >instructions</a
              >
              when you are creating your own bot!
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <div class="errorMessage">{{ botInfo.errorMessage }}</div>
          <button
            type="button"
            class="btn btn-secondary"
            data-bs-dismiss="modal"
          >
            Close
          </button>
          <button type="button" class="btn btn-primary" @click="updateBot">
            Save
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import $ from 'jquery'
import { useStore } from 'vuex'
import { reactive, watch, ref } from 'vue'
import { Modal } from 'bootstrap/dist/js/bootstrap'
import { VAceEditor } from 'vue3-ace-editor'
import ace from 'ace-builds'
import { domainNameProd } from '@/global'

export default {
  components: {
    VAceEditor,
  },
  props: ['id'],
  setup(props, context) {
    ace.config.set(
      'basePath',
      'https://cdn.jsdelivr.net/npm/ace-builds@' +
        require('ace-builds').version +
        '/src-noconflict/',
    )

    const store = useStore()
    const id = ref()
    const botInfo = reactive({
      id: '',
      name: '',
      description: '',
      code: '',
      errorMessage: '',
    })

    const getInfo = (id) => {
      $.ajax({
        url: `${domainNameProd}/api/user/bot/info`,
        type: 'get',
        data: {
          id: id,
        },
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        success(resp) {
          botInfo.id = resp.id
          botInfo.name = resp.name
          botInfo.description = resp.description
          botInfo.code = resp.code
          botInfo.errorMessage = resp.errorMessage
          context.emit('gotten')
        },
      })
    }

    const updateBot = () => {
      botInfo.errorMessage = ''
      $.ajax({
        url: `${domainNameProd}/api/user/bot/edit`,
        type: 'post',
        data: {
          id: botInfo.id,
          name: botInfo.name,
          description: botInfo.description,
          code: botInfo.code,
        },
        headers: {
          Authorization: 'Bearer ' + store.state.user.token,
        },
        success(resp) {
          if (resp.errorMessage === 'success') {
            Modal.getInstance('#editBot').hide()
            context.emit('edited')
          } else botInfo.errorMessage = resp.errorMessage
        },
      })
    }

    watch(
      () => props.id,
      () => {
        id.value = props.id
        if (props.id != null) getInfo(id.value)
      },
    )

    return {
      botInfo,
      updateBot,
      getInfo,
    }
  },
}
</script>

<style scoped>
.errorMessage {
  color: red;
}
</style>
