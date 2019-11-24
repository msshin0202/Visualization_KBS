handleSubmit = () => {
  $(document).ready(function() {
    url = $("input.form-input").val();
    $.post(
      "http://localhost:8080/backend/rest/getDependency",
      {"url": url},
      dataType="json")
      .done(res => {
        console.log(res);
      })
  })
}