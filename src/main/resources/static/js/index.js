let page = 1;
const size = 5;
const container = document.getElementById("thread-container");

window.addEventListener("scroll", () => {
    if (window.innerHeight + window.scrollY >= document.body.offsetHeight) {
        loadThreads();
    }
});

const loadThreads = async () => {
    const response = await fetch(`/api/threads?page=${page}&size=${size}`);
    const threads = await response.json();

    let notZeroThread = false;


    threads.forEach(thread => {
        notZeroThread = true;
        const threadElement = document.createElement("div");
        threadElement.className = "card mb-4 ms-5 me-5";
        threadElement.innerHTML = `
            <div class="card-body">
                <div class="small text-muted">${thread.firstMessageID} ${new Date(thread.dateTime).toLocaleString()}</div>
                <h2 class="card-title">${thread.head_thread}</h2>
                <p class="card-text">${thread.body}</p>
                <a class="btn btn-primary btn-sm" href="thread/${thread.id}">Ответить</a>
            </div>
        `;
        container.appendChild(threadElement);
    });
    page = notZeroThread ? page + 1 : page;
};