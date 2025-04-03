let allFiles = [];

setupDropZone('dropZoneTop', 'fileInputTop', 'fileListTop');
setupDropZone('dropZoneBottom', 'fileInputBottom', 'fileListBottom');

function setupDropZone(dropZoneId, fileInputId, fileListId) {
    const dropZone = document.getElementById(dropZoneId);
    const fileInput = document.getElementById(fileInputId);
    const fileList = document.getElementById(fileListId);

    // Перетаскивание файлов
    dropZone.addEventListener('dragover', (event) => {
        event.preventDefault();
        dropZone.classList.add('border-primary');
    });

    dropZone.addEventListener('dragleave', () => {
        dropZone.classList.remove('border-primary');
    });

    dropZone.addEventListener('drop', (event) => {
        event.preventDefault();
        dropZone.classList.remove('border-primary');
        const files = Array.from(event.dataTransfer.files);
        addFiles(files, fileList);
    });


    dropZone.addEventListener('click', (event) => {
        // Проверяем, что клик был именно по dropZone, а не по кнопке
        if (event.target === dropZone && !dropZone.classList.contains('processing')) {
            dropZone.classList.add('processing');
            fileInput.click();
            setTimeout(() => dropZone.classList.remove('processing'), 100);
        }
    });


    fileInput.addEventListener('change', (event) => {
        const files = Array.from(event.target.files);
        addFiles(files, fileList);
    });


    const form = dropZone.closest('form');
    form.addEventListener('submit', (event) => {

        const dataTransfer = new DataTransfer();


        allFiles.forEach(file => {
            dataTransfer.items.add(file);
        });


        fileInput.files = dataTransfer.files;
    });
}


function addFiles(files, fileList) {
    const MAX_FILE_SIZE = 25 * 1024 * 1024;

    const fileArray = Array.from(files);

    fileArray.forEach((file) => {


        if (file.size > MAX_FILE_SIZE) {
            alert(`Файл "${file.name}" превышает максимально допустимый размер 50 МБ.`);
            return;
        }

        if (file.type.startsWith('image/') || file.type.startsWith('video/')) {

            if (allFiles.length >= 4) {
                alert('Вы можете прикрепить не более 4 файлов.');
                return;
            }

            if (!allFiles.some((f) => f.name === file.name && f.size === file.size)) {
                allFiles.push(file);

                const filePreview = document.createElement('div')

                makePreview(filePreview, file);

                fileList.appendChild(filePreview);
            }
        } else {
            alert(`Файл ${file.name} не поддерживается.`);
        }
    });
}

function topButton() {
    var messageFormBottom = document.getElementById('messageFormBottom');
    if (messageFormBottom.classList.contains('collapsing')) {
        const messageAreaTop = document.getElementById('messageAreaTop');
        const messageAreaBot = document.getElementById('messageAreaBot');
        const fileInputBottom = document.getElementById('fileInputBottom');
        //const fileListTop = document.getElementById('fileListTop').querySelector('ul');

        // Синхронизация текста
        messageAreaTop.value = messageAreaBot.value;

        // Синхронизация файлов
        syncFiles(Array.from(fileInputBottom.files));
        updateFileList('fileListTop');
    }
}

function botButton() {
    var messageFormBottom = document.getElementById('messageFormTop');
    if (messageFormBottom.classList.contains('collapsing')) {
        const messageAreaTop = document.getElementById('messageAreaTop');
        const messageAreaBot = document.getElementById('messageAreaBot');
        const fileInputTop = document.getElementById('fileInputTop');
        //const fileListBottom = document.getElementById('fileListBottom').querySelector('ul');

        // Синхронизация текста
        messageAreaBot.value = messageAreaTop.value;

        // Синхронизация файлов
        syncFiles(Array.from(fileInputTop.files));
        updateFileList('fileListBottom');
    }
}

function syncFiles(files) {
    files.forEach((file) => {
        if (!allFiles.some((f) => f.name === file.name && f.size === file.size)) {
            allFiles.push(file);
        }
    });
}

function updateFileList(listId) {
    const fileList = document.getElementById(listId);
    fileList.innerHTML = ''; // Очищаем список

    allFiles.forEach((file) => {

        const filePreview = document.createElement('div');

        makePreview(filePreview, file);

        fileList.appendChild(filePreview);
    });
}

function makePreview(filePreview, file) {
    filePreview.className = 'border p-1 rounded'; // Для аккуратного оформления
    filePreview.style.width = '100px';
    filePreview.style.height = '100px';
    filePreview.style.overflow = 'hidden';
    filePreview.style.display = 'flex';
    filePreview.style.alignItems = 'center';
    filePreview.style.justifyContent = 'center';

    if (file.type.startsWith('image/')) {
        // Превью изображения
        const img = document.createElement('img');
        img.src = URL.createObjectURL(file);
        img.alt = file.name;
        img.style.width = '100%';
        img.style.height = '100%';
        img.style.objectFit = 'cover';
        img.className = 'rounded';
        filePreview.appendChild(img);
    } else if (file.type.startsWith('video/')) {
        // Превью видео
        const video = document.createElement('video');
        video.src = URL.createObjectURL(file);
        video.controls = true;
        video.style.width = '100%';
        video.style.height = '100%';
        video.className = 'rounded';
        filePreview.appendChild(video);
    }
}

function loadFullImage(thumbnail){
    loadFullFile(thumbnail, 'img');
}

function loadFullVideo(thumbnail){
    loadFullFile(thumbnail, 'video');
}

function loadFullFile(thumbnail, fileType) {

    const fullUrl = thumbnail.getAttribute('data-full-url');

    const modal = document.createElement('div');
    modal.classList.add('full-image-modal');

    const fullImage = document.createElement(fileType);

    if (fileType === 'video') {
        fullImage.setAttribute('controls', ''); // Показываем элементы управления
        fullImage.setAttribute('autoplay', '');  // Опционально автозапуск
    }
    fullImage.src = fullUrl;

    modal.appendChild(fullImage);

    modal.addEventListener('click', function () {
        document.body.removeChild(modal);
    });

    document.body.appendChild(modal);
}

