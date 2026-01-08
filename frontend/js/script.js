$(document).ready(function () {
  getAllCustomers();

  $("#customer-form").on("submit", function (e) {
    e.preventDefault();

    const id = $("#customerId").val();
    const name = $("#customerName").val();
    const address = $("#customerAddress").val();

    $.ajax({
      url: "http://localhost:8080/backend_ee_Web_exploded/api/v1/customer",
      method: "POST",
      contentType: "application/json",
      data: JSON.stringify({
        id: id,
        name: name,
        address: address,
      }),
      success: function (response) {
        console.log(response);
        getAllCustomers();
        $("#customerId").val("");
        $("#customerName").val("");
        $("#customerAddress").val("");
      },
      error: function (error) {
        console.log(error);
      },
    });
  });

  function getAllCustomers() {
    $.ajax({
      url: "http://localhost:8080/backend_ee_Web_exploded/api/v1/customer",
      method: "GET",
      success: function (response) {
        $("#customer-grid").empty();
        response.forEach(function (customer) {
          const newCard = `<div class="data-card">
                                        <div class="data-card-icon">
                                            <i class="fa fa-user"></i>
                                        </div>
                                        <div class="data-card-content">
                                            <div class="info-item">
                                                <span class="tag tag-blue">Customer ID</span>
                                                <span class="tag-value">${customer.id}</span>
                                            </div>
                                            <div class="info-item">
                                                <span class="tag tag-green">Name</span>
                                                <span class="tag-value">${customer.name}</span>
                                            </div>
                                            <div class="info-item">
                                                <span class="tag tag-purple">Address</span>
                                                <span class="tag-value">${customer.address}</span>
                                            </div>
                                        </div>
                                    </div>`;
          $("#customer-grid").append(newCard);
        });
      },
      error: function (error) {
        console.log(error);
      },
    });
  }

  getAllItems();

  $("#item-form").on("submit", function (e) {
    e.preventDefault();

    const id = $("#itemId").val();
    const name = $("#itemName").val();
    const price = $("#itemPrice").val();

    $.ajax({
      url: "http://localhost:8080/backend_ee_Web_exploded/api/v1/item",
      method: "POST",
      contentType: "application/json",
      data: JSON.stringify({
        id: id,
        name: name,
        price: price,
      }),
      success: function (response) {
        console.log(response);
        getAllItems();
        $("#itemId").val("");
        $("#itemName").val("");
        $("#itemPrice").val("");
      },
      error: function (error) {
        console.log(error);
      },
    });
  });

  function getAllItems() {
    $.ajax({
      url: "http://localhost:8080/backend_ee_Web_exploded/api/v1/item",
      method: "GET",
      success: function (response) {
        $("#item-grid").empty();
        response.forEach(function (item) {
          const newCard = `<div class="data-card">
                                        <div class="data-card-icon">
                                            <i class="fa fa-tag"></i>
                                        </div>
                                        <div class="data-card-content">
                                            <div class="info-item">
                                                <span class="tag tag-blue">Item ID</span>
                                                <span class="tag-value">${item.id}</span>
                                            </div>
                                            <div class="info-item">
                                                <span class="tag tag-green">Name</span>
                                                <span class="tag-value">${item.name}</span>
                                            </div>
                                            <div class="info-item">
                                                <span class="tag tag-orange">Price</span>
                                                <span class="tag-value">$${item.price}</span>
                                            </div>
                                        </div>
                                    </div>`;
          $("#item-grid").append(newCard);
        });
      },
      error: function (error) {
        console.log(error);
      },
    });
  }

  getAllOrders();

  $("#order-form").on("submit", function (e) {
    e.preventDefault();

    const id = $("#orderId").val();
    const customerId = $("#orderCustomerId").val();
    const itemId = $("#orderItemId").val();

    $.ajax({
      url: "http://localhost:8080/backend_ee_Web_exploded/api/v1/order",
      method: "POST",
      contentType: "application/json",
      data: JSON.stringify({
        id: id,
        customerId: customerId,
        itemId: itemId,
      }),
      success: function (response) {
        console.log(response);
        getAllOrders();
        $("#orderId").val("");
        $("#orderCustomerId").val("");
        $("#orderItemId").val("");
      },
      error: function (error) {
        console.log(error);
      },
    });
  });

  function getAllOrders() {
    $.ajax({
      url: "http://localhost:8080/backend_ee_Web_exploded/api/v1/order",
      method: "GET",
      success: function (response) {
        $("#order-grid").empty();
        response.forEach(function (order) {
          const newCard = `<div class="data-card">
                                        <div class="data-card-icon">
                                            <i class="fa fa-shopping-cart"></i>
                                        </div>
                                        <div class="data-card-content">
                                            <div class="info-item">
                                                <span class="tag tag-blue">Order ID</span>
                                                <span class="tag-value">${order.id}</span>
                                            </div>
                                            <div class="info-item">
                                                <span class="tag tag-green">Customer ID</span>
                                                <span class="tag-value">${order.customerId}</span>
                                            </div>
                                            <div class="info-item">
                                                <span class="tag tag-purple">Item ID</span>
                                                <span class="tag-value">${order.itemId}</span>
                                            </div>
                                        </div>
                                    </div>`;
          $("#order-grid").append(newCard);
        });
      },
      error: function (error) {
        console.log(error);
      },
    });
  }
});
