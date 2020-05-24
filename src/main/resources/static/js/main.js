function getIndex(queue, id) {
    for (var i = 0; i < queue.length; i++ ) {
        if (queue[i].id === id) {
            return i;
        }
    }

    return -1;
}

var ordererApi = Vue.resource('/orderer{/id}');

Vue.component('orderer-form', {
    props: ['orderers', 'ordererAttr'],
    data: function() {
        return {
            name: '',
            text: '',
            creationTime: '',
            id: ''
        }
    },
    watch: {
        ordererAttr: function(newVal, oldVal) {
            this.name = newVal.name;
            this.text=newVal.text;
            this.creationTime=newVal.creationTime;
            this.id = newVal.id;
        }
    },
    template:
        '<div>' +
        '<input type="text" placeholder="Write Name" v-model="name" />' +
        '<input type="text" placeholder="Write Text" v-model="text" />' +
        '<input type="button" value="Save" @click="save" />' +
        '</div>',
    methods: {
        save: function() {
            var orderer = { name: this.name,
                text: this.text,
                creationTime: this.creationTime};

            if (this.id) {
                ordererApi.update({id: this.id}, orderer).then(result =>
                    result.json().then(data => {
                        var index = getIndex(this.orderer, data.id);
                        this.orderer.splice(index, 1, data);
                        this.name = '';
                        this.text = '';
                        this.creationTime = '';
                        this.id = ''
                    })
                )
            } else {
                ordererApi.save({}, orderer).then(result =>
                    result.json().then(data => {
                        this.orderer.push(data);
                        this.name = '';
                        this.creationTime = '';
                        this.text = ''
                    })
                )
            }
        }
    }
});

Vue.component('orderer-row', {
    props: ['orderer', 'editMethod', 'orderers'],
    template: '<div>' +
        '<i>({{ orderer.id }})</i> {{ orderer.name }} {{ orderer.text }} {{orderer.creationTime}}' +
        '<span style="position: absolute; right: 0">' +
        '<input type="button" value="Edit" @click="edit" />' +
        '<input type="button" value="del" @click="del" />' +
        '</span>' +
        '</div>',
    methods: {
        edit: function() {
            this.editMethod(this.orderer);
        },
        del: function() {
            ordererApi.remove({id: this.orderer.id}).then(result => {
                if (result.ok) {
                    this.orderers.splice(this.orderers.indexOf(this.orderer), 1)
                }
            })
        }
    }
});

Vue.component('orderer-queue', {
    props: ['orderers'],
    data: function() {
        return {
            orderer: null
        }
    },
    template:
        '<div style="position: relative; width: 450px;">' +
        '<orderer-form :orderers="orderers" :ordererAttr="orderer" />' +
        '<orderer-row v-for="orderer in orderers" :k:orderer="orderer.id" :orderer="orderer" ' +
        ':editMethod="editMethod" :orderers="orderers" />' +
        '</div>',
    created: function() {
        ordererApi.get().then(result =>
            result.json().then(data =>
                data.forEach(orderer => this.orderers.push(orderer))
            )
        )
    },
    methods: {
        editMethod: function(orderer) {
            this.orderer = orderer;
        }
    }
});

var app = new Vue({
    el: '#app',
    template: '<orderer-queue :orderers="orderers" />',
    data: {
        orderers: []
    }
});