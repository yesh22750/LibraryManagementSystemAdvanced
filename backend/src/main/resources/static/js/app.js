const API = "http://localhost:8080/api";

/* =========================
API STATUS
========================= */

async function checkApi() {
const status = document.getElementById("apiStatus");

try {
    const response = await fetch(`${API}/health`);

    if (!response.ok) {
        throw new Error("API unavailable");
    }

    status.textContent = "● API Connected";
    status.style.background = "#16a34a";

} catch (error) {

    status.textContent = "● API Offline";
    status.style.background = "#dc2626";

    console.error(error);
}

}

/* =========================
SECTION NAVIGATION
========================= */

function showSection(sectionId) {

document.querySelectorAll(".section").forEach(section => {
    section.classList.remove("active");
});

const section = document.getElementById(sectionId);

if (section) {
    section.classList.add("active");
}

if (sectionId === "dashboard") {
    loadBooks();
}

if (sectionId === "booksSection") {
    loadBooksPage();
}

if (sectionId === "usersSection") {
    loadUsers();
}

if (sectionId === "transactionsSection") {
    loadTransactions();
}

}

/* =========================
BOOKS
========================= */

async function loadBooks() {

const container = document.getElementById("books");

if (!container) {
    return;
}

const searchInput = document.getElementById("search");

const query = searchInput
    ? searchInput.value.trim()
    : "";

try {

    const url = query
        ? `${API}/books?q=${encodeURIComponent(query)}`
        : `${API}/books`;

    const response = await fetch(url);

    if (!response.ok) {
        throw new Error("Unable to load books");
    }

    const books = await response.json();

    displayBooks(container, books);

    updateBookStatistics(books);

} catch (error) {

    container.innerHTML = `
        <p>Unable to load books.</p>
    `;

    console.error(error);
}

}

/* =========================
BOOK PAGE
========================= */

async function loadBooksPage() {

const container = document.getElementById("booksList");

if (!container) {
    return;
}

try {

    const response = await fetch(`${API}/books`);

    if (!response.ok) {
        throw new Error("Unable to load books");
    }

    const books = await response.json();

    displayBooks(container, books);

} catch (error) {

    container.innerHTML = `
        <p>Unable to load books.</p>
    `;

    console.error(error);
}

}

/* =========================
DISPLAY BOOKS
========================= */

function displayBooks(container, books) {

if (books.length === 0) {

    container.innerHTML = `
        <p>No books found.</p>
    `;

    return;
}

container.innerHTML = books.map(book => {

    const availableClass =
        book.availableCopies > 0
            ? "available"
            : "unavailable";

    return `
        <div class="book-card">

            <h3>${escapeHtml(book.title)}</h3>

            <p>
                <strong>Book ID:</strong>
                ${book.bookId}
            </p>

            <p>
                <strong>ISBN:</strong>
                ${escapeHtml(book.isbn)}
            </p>

            <p>
                <strong>Author:</strong>
                ${escapeHtml(book.author?.authorName || "Unknown")}
            </p>

            <p>
                <strong>Category:</strong>
                ${escapeHtml(book.category?.categoryName || "Unknown")}
            </p>

            <p>
                <strong>Publication Year:</strong>
                ${book.publicationYear || "-"}
            </p>

            <p>
                <strong>Shelf:</strong>
                ${escapeHtml(book.shelfLocation || "-")}
            </p>

            <p class="${availableClass}">
                Available:
                ${book.availableCopies}
                /
                ${book.totalCopies}
            </p>

        </div>
    `;

}).join("");

}

/* =========================
BOOK STATISTICS
========================= */

function updateBookStatistics(books) {

const bookCount =
    document.getElementById("bookCount");

const availableCount =
    document.getElementById("availableCount");

if (bookCount) {
    bookCount.textContent = books.length;
}

if (availableCount) {

    const totalAvailable = books.reduce(
        (sum, book) =>
            sum + Number(book.availableCopies || 0),
        0
    );

    availableCount.textContent = totalAvailable;
}

}

/* =========================
USERS
========================= */

async function loadUsers() {

const container =
    document.getElementById("users");

if (!container) {
    return;
}

try {

    const response =
        await fetch(`${API}/users`);

    if (!response.ok) {
        throw new Error("Unable to load users");
    }

    const users = await response.json();

    const userCount =
        document.getElementById("userCount");

    if (userCount) {
        userCount.textContent = users.length;
    }

    if (users.length === 0) {

        container.innerHTML =
            "<p>No users found.</p>";

        return;
    }

    container.innerHTML = `

        <table>

            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Membership Date</th>
                    <th>Status</th>
                </tr>
            </thead>

            <tbody>

                ${users.map(user => `

                    <tr>

                        <td>${user.userId}</td>

                        <td>
                            ${escapeHtml(user.fullName)}
                        </td>

                        <td>
                            ${escapeHtml(user.email)}
                        </td>

                        <td>
                            ${escapeHtml(user.phone || "-")}
                        </td>

                        <td>
                            ${user.membershipDate || "-"}
                        </td>

                        <td>
                            ${user.status}
                        </td>

                    </tr>

                `).join("")}

            </tbody>

        </table>
    `;

} catch (error) {

    container.innerHTML =
        "<p>Unable to load users.</p>";

    console.error(error);
}

}

/* =========================
TRANSACTIONS
========================= */

async function loadTransactions() {

const container =
    document.getElementById("transactions");

if (!container) {
    return;
}

try {

    const response =
        await fetch(`${API}/transactions`);

    if (!response.ok) {
        throw new Error("Unable to load transactions");
    }

    const transactions =
        await response.json();

    const transactionCount =
        document.getElementById("transactionCount");

    if (transactionCount) {
        transactionCount.textContent =
            transactions.length;
    }

    if (transactions.length === 0) {

        container.innerHTML =
            "<p>No transactions found.</p>";

        return;
    }

    container.innerHTML = `

        <table>

            <thead>

                <tr>
                    <th>ID</th>
                    <th>Book</th>
                    <th>User</th>
                    <th>Issue Date</th>
                    <th>Due Date</th>
                    <th>Return Date</th>
                    <th>Status</th>
                    <th>Fine</th>
                </tr>

            </thead>

            <tbody>

                ${transactions.map(transaction => `

                    <tr>

                        <td>
                            ${transaction.transactionId}
                        </td>

                        <td>
                            ${escapeHtml(
                                transaction.book?.title || "-"
                            )}
                        </td>

                        <td>
                            ${escapeHtml(
                                transaction.user?.fullName || "-"
                            )}
                        </td>

                        <td>
                            ${transaction.issueDate || "-"}
                        </td>

                        <td>
                            ${transaction.dueDate || "-"}
                        </td>

                        <td>
                            ${transaction.returnDate || "-"}
                        </td>

                        <td>
                            ${transaction.status}
                        </td>

                        <td>
                            ₹${transaction.fineAmount || 0}
                        </td>

                    </tr>

                `).join("")}

            </tbody>

        </table>
    `;

} catch (error) {

    container.innerHTML =
        "<p>Unable to load transactions.</p>";

    console.error(error);
}

}

/* =========================
ISSUE BOOK
========================= */

async function issueBook() {

const bookId =
    Number(document.getElementById("issueBookId").value);

const userId =
    Number(document.getElementById("issueUserId").value);

const result =
    document.getElementById("issueResult");

if (!bookId || !userId) {

    result.innerHTML =
        "Please enter both Book ID and User ID.";

    return;
}

try {

    const response = await fetch(
        `${API}/library/issue`,
        {
            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                bookId: bookId,
                userId: userId
            })
        }
    );

    if (!response.ok) {

        const errorText =
            await response.text();

        throw new Error(
            errorText || "Unable to issue book"
        );
    }

    const data =
        await response.json();

    result.innerHTML = `

        <strong>Book issued successfully!</strong>

        <br>
        Transaction ID: ${data.transactionId}

        <br>
        Book: ${escapeHtml(data.bookTitle)}

        <br>
        User: ${escapeHtml(data.userName)}

        <br>
        Issue Date: ${data.issueDate}

        <br>
        Due Date: ${data.dueDate}

        <br>
        Status: ${data.status}
    `;

    document.getElementById("issueBookId").value = "";
    document.getElementById("issueUserId").value = "";

} catch (error) {

    result.innerHTML = `
        <strong>Error:</strong>
        ${escapeHtml(error.message)}
    `;

    console.error(error);
}

}

/* =========================
RETURN BOOK
========================= */

async function returnBook() {

const transactionId =
    Number(
        document.getElementById(
            "returnTransactionId"
        ).value
    );

const result =
    document.getElementById("returnResult");

if (!transactionId) {

    result.innerHTML =
        "Please enter a Transaction ID.";

    return;
}

try {

    const response = await fetch(
        `${API}/library/return/${transactionId}`,
        {
            method: "POST"
        }
    );

    if (!response.ok) {

        const errorText =
            await response.text();

        throw new Error(
            errorText || "Unable to return book"
        );
    }

    const data =
        await response.json();

    result.innerHTML = `

        <strong>Book returned successfully!</strong>

        <br>
        Transaction ID: ${data.transactionId}

        <br>
        Book: ${escapeHtml(data.bookTitle)}

        <br>
        User: ${escapeHtml(data.userName)}

        <br>
        Return Date: ${data.returnDate}

        <br>
        Fine: ₹${data.fineAmount}

        <br>
        Status: ${data.status}
    `;

    document.getElementById(
        "returnTransactionId"
    ).value = "";

} catch (error) {

    result.innerHTML = `
        <strong>Error:</strong>
        ${escapeHtml(error.message)}
    `;

    console.error(error);
}

}

/* =========================
HTML SAFETY
========================= */

function escapeHtml(value) {

return String(value)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");

}

/* =========================
INITIAL LOAD
========================= */

document.addEventListener(
"DOMContentLoaded",
() => {

    checkApi();
    loadBooks();
    loadUsers();
    loadTransactions();

}

);
