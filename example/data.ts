export const commentReceiptHtml = `<html lang="en">
  <head>
    <meta charset="utf-8" />
    <title>Haravan</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <style>
      * {
        margin: 0;
        padding: 0;
        border: 0;
        font-size: 100%;
        font: inherit;
        vertical-align: baseline;
        color: #212121;
      }
      html {
        font-size: 16px; /* Base font size for rem calculations */
      }
      html,
      body {
        font-family: sans-serif;
        -webkit-text-size-adjust: 100%;
      }
      .page-name {
        font-size: 35px;
        font-weight: 600;
        flex: 1;
      }
      .date {
        font-size: 30px;
        font-weight: 400;
      }
      .header {
        margin-bottom: 12px;
      }
      .header-top {
        display: flex;
        align-items: center;
        margin-bottom: 2px;
      }
      .header-bottom {
        display: flex;
        align-items: center;
      }
      .campaign-name {
        font-size: 30px;
        font-weight: 500;
        flex: 1;
      }
      .print-id {
        font-size: 35px;
        font-weight: 700;
      }
      .text-8-normal {
        font-size: 20px;
        font-weight: 400;
      }
      .text-10-semiBold {
        font-size: 25px;
        font-weight: 500;
      }
      .text-10-normal {
        font-size: 25px;
        font-weight: 400;
      }
      .text-12-semiBold {
        font-size: 30px;
        font-weight: 600;
      }
      .text-12-normal {
        font-size: 30px;
        font-weight: 400;
      }
      .info {
        margin-bottom: 8px;
      }
      .customer-phone {
        display: flex;
        flex-direction: row;
        align-items: center;
        column-gap: 2px;
      }
      .flex-col-center {
        display: flex;
        flex-direction: column;
        align-items: center;
      }
      .customer-name {
        font-size: 40px;
        font-weight: 700;
        margin-bottom: 4px;
      }
      .products {
        border-top: 1px dashed #000000;
      }
      .product-item {
        padding-top: 6px;
        padding-bottom: 6px;
        border-bottom: 1px dashed #000000;
      }
      .product-item:last-child {
        border-bottom: 0px;
        padding-bottom: 0px;
      }
      .product-item-info {
        display: flex;
        align-items: center;
        margin-bottom: 4px;
      }
      .product-item-left {
        flex: 1;
      }
      .product-item-right {
        max-width: 200px;
      }
      .comment {
        padding-top: 6px;
        padding-bottom: 6px;
      }
      .qr-code {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        padding-top: 12px;
        padding-bottom: 12px;
        border-top: 1px solid #000000;
      }
      .footer {
        display: flex;
        align-items: center;
        column-gap: 12px;
        justify-content: space-between;
        padding-top: 12px;
        padding-bottom: 12px;
        border-top: 1px solid #000000;
      }
      .username {
        font-size: 25px;
        font-weight: 600;
      }
    </style>
  </head>
  <body>
    <div>
      <div class="header">
        <div class="header-top">
          <h3 class="page-name">Yêu màu hồng</h3>
          <p class="date">10:12, T3 27/05</p>
        </div>
        <div class="header-bottom">
          <p class="campaign-name">Campaign 20/11/2024 03:03 CH</p>
          <h3 class="print-id">#1</h3>
        </div>
      </div>

      <div class="info">
        <div class="flex-col-center" style="margin-bottom: 0.5rem">
          <p class="customer-name">Vu Nguyen</p>
          <p class="customer-phone">
            <span class="text-12-semiBold">0977066212</span>
            <span class="text-10-normal">(Viettel)</span>
          </p>
        </div>
        <div class="flex-col-center">
          <p class="text-10-normal">
            Địa chỉ:
            <span class="text-10-semiBold"
              >4584339843, Xã Hoà Lạc, Huyện Phú Tân, An Giang</span
            >
          </p>
        </div>
      </div>

      <div class="products">
        <div class="product-item">
          <div class="product-item-info">
            <div class="product-item-left">
              <p class="text-10-semiBold" style="margin-bottom: 0.125rem">
                1. ở đây là một trong những ngày đầu năm mới gọi là đi tới 3 2
                cắt lê hồng phong quẹo
              </p>
              <p class="text-10-normal">Default Title</p>
            </div>
            <div class="product-item-right">
              <p class="text-10-semiBold" style="text-align: right">
                5,888 x 1
              </p>
            </div>
          </div>
        </div>
      </div>

      <div class="comment">
        <p class="text-12-normal">A4</p>
      </div>

      <div class="qr-code" data-type="qrcode" data-size="200" data-error-correction="L">0977066122</div>
      <div class="qr-code" data-type="barcode" data-width="200" data-height="80">0977066122</div>


      <div
        class="cssMobile"
        style="margin-top: 0; margin-bottom: 0.625rem; display: none"
      >
        <p style="border-bottom: 2px solid red; height: 1px"></p>
      </div>
    </div>
  </body>
</html>
`;

export const generateShipmentHtml = (length: number = 10) => `

<head>
<meta charset="utf-8" />
<title>Haravan</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<style>
  @page {
    margin: 0;
  }
  html,
  body {
    width: 100%;
    margin: 0;
    font-family: sans-serif;
  }
  .table {
    font-size: 30px !important;
    float: left;
  }
  .printorder {
    margin: 0 auto;
    margin-bottom: 20px;
    padding: 10;
  }
  .tbl-style {
    width: 100%;
  }
  .text-right {
    text-align: right;
  }
  .customers {
    float: left;
    text-align: center;
    width: 100%;
  }
  .configShop {
    float: left;
    text-align: left;
    width: 100%;
  }
  .customer-name {
    font-weight: 700;
    font-size: 45px !important;
    text-align: center;
    margin: 0;
    margin-top: 14px;
    margin-bottom: 10;
  }
  .customer-phone,
  .customer-address {
    font-weight: 600;
    margin: 12px 0px;
    font-size: 35px !important;
    text-align: center;
  }
  .line-footer {
    margin-top: 0px;
    margin-bottom: 6px;
    float: left;
    width: 100%;
  }
  .pageName {
    font-weight: 600;
    color: "#212121";
    font-size: 35px !important;
    margin: 10 0 10 0;
  }
  .orderNumber {
    font-size: 35px !important;
    font-weight: 600;
    color: #212121;
  }
  .comment-title {
    text-align: center;
    font-weight: bold;
    margin-top: 0;
    margin-bottom: 8px;
    font-size: 30px !important;
  }
  .comment {
    margin: 10 0;
    text-align: justify;
    word-wrap: break-word;
    margin-top: 0;
    font-size: 30px !important;
  }
  .itemProduct {
    border-bottom: 1px dashed #000000;
    float: left;
    width: 100%;
  }
  .itemProduct * {
    padding: 0;
    margin: 6px 0px;
  }
  .productName {
    float: left;
    width: 100%;
    font-size: 30px !important;
    margin: 0;
    font-weight: 600;
  }
  .productPrice {
    font-size: 30px !important;
    margin: 0;
    font-weight: 600;
  }
  .tblLeft {
    width: 60%;
    margin-right: 10%;
    float: left;
  }
  .tblRight {
    width: 30%;
    float: right;
  }
  .variantTitle {
    float: left;
    font-size: 30px !important;
    margin: 0;
    margin-top: 4px;
  }
  .shopItem {
    margin: 4 0;
    font-size: 30px !important;
    font-weight: 500;
  }
  .printorder * {
    font-size: 30px;
  }
  .tbl-style tr:last-child {
    border-bottom: none;
  }
  .footer {
    float: left;
    width: 100%;
    display: flex;
    flex-wrap: nowrap;
    justify-content: center;
  }
  .footer-text {
    font-size: 30px !important;
  }
  .sort-code {
    font-size: 30px !important;
  }
  .tracking-number {
    font-size: 30px !important;
  }
  .printrow {
    clear: both;
    width: 100%;
    display: inline-table;
  }
</style>
</head>
<body>
<div id="printOrder" class="printorder">
  <div class="table">
    <div class="configShop">
      <p class="pageName">Havavan Campaign</p>
      <p class="shopItem">Hotline: 1900.636.099</p>
      <p class="shopItem">
        Địa chỉ shop: Tầng 6, Tòa nhà Flemington, 182 Lê Đại Hành, P.15,
        Q.11, Hồ Chí Minh.
      </p>
      <p class="shopItem">NV tạo: Nhân Viên 1 - 26/06/2024 09:00:00</p>
    </div>
  </div>
  <div class="customers">
    <div
      class="printtable"
      style="
        float: left;
        width: 100%;
        text-align: center;
        padding: 8px 0px 8px 0px;
      "
    >
      <p class="tracking-number" style="margin: 2 0">Best Express</p>
      <img
        src="https://store-api.hararetail.com/api/barcode/code128?text=84857833177633"
      style="height: 60px; width: 300px"
      />
      <p class="tracking-number" style="margin: 2 0 2 0">84857833177633</p>
    </div>

    <p class="customer-name">Thu hộ COD 495.000</p>
    <p class="customer-address">
      Tiền hàng: 465.000 - Khuyến mãi: 10.000 + Phí ship: 30.000 (Đã trả
      trước: 200.000)
    </p>
  </div>
  <div style="float: left; font-size: 15px">
    <p class="shopItem"><b>Người nhận </b>: Võ Văn Hoài</p>
    <p class="shopItem">
      <b>Địa chỉ: </b> 182 Lê Đại Hành, P.15, Q.11, Hồ Chí Minh.
    </p>
    <p class="shopItem"><b>Điện thoại: </b> 0962600004</p>
  </div>
  <table class="tbl-style" style="float: left">
    <tbody>
      ${Array.from({ length })
        .map(
          (_, index) => `
          <tr class="itemProduct">
          <td class="tblLeft">
            <p class="productName">${index + 1}. Nike Air Presto Mid Utility</p>
            <p class="variantTitle">Size 42 / Đen</p>
          </td>
          <td class="tblRight text-right">
            <p class="productPrice">250.000 x 1</p>
          </td>
        </tr>
        `
        )
        .join("")}
    </tbody>
  </table>

  <div style="float: left; margin: 8px 0px; width: 100%">
    <p class="comment-title">Ghi chú giao hàng</p>
    <div class="printrow" style="white-space: pre-line; text-align: center">
      <p class="comment">
        Xem hàng không nhận thu 30k Ship( nộp tiền sửa COD). Hotline:
        0971329778.
      </p>
    </div>
    <div class="printrow" style="white-space: pre-line; text-align: center">
      <p class="comment">
        2 BALO TRONG TIN NHĂN ( QUÀ TẶNG ) ĐÃ TRỪ CỌC 100K TC 2
      </p>
    </div>
  </div>
  <div
    class="printtable"
    style="float: left; width: 100%; text-align: center; padding: 0"
  >
    <img
      src="https://store-api.hararetail.com/api/barcode/code128?text=12893797240"
      style="height: 60px; width: 300px"
    />
    <p class="sort-code" style="margin: 2 0">12893797240</p>
    <p></p>
  </div>
  <div class="footer">
    <span class="footer-text"
      >Haravan - Giải pháp bán hàng Livestream
    </span>
  </div>
  <div
    class="cssMobile"
    style="
      margin-top: 0px;
      margin-bottom: 0px;
      display: none;
      float: left;
      width: 100%;
    "
  >
    <div
      style="border-bottom: 2px solid red; height: 1px; margin: 0px"
    ></div>
  </div>
</div>
</body>
</html>
`;

export const exampleHtml = `
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Responsive WebView Page</title>
  <style>
    /* Reset default margins and paddings */
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }

    body {
      font-family: Arial, sans-serif;
      padding: 20px;
      line-height: 1.6;
    }

    h1 {
      font-size: 24px;
      margin-bottom: 10px;
    }

    p {
      font-size: 16px;
      margin-bottom: 10px;
    }

    /* Ensure images scale properly on different screen sizes */
    img {
      max-width: 100%;
      height: auto;
      display: block;
      margin-bottom: 10px;
    }

    /* Example of a responsive container */
    .container {
      max-width: 600px;
      margin: 0 auto;
    }
  </style>
</head>
<body>
  <div class="container">
    <h1>Welcome to the WebView</h1>
    <p>This is a sample HTML page optimized for display within an Android WebView.</p>
    <p>Resize the window or view on different devices to see responsive behavior.</p>
  </div>
</body>
</html>
`;
