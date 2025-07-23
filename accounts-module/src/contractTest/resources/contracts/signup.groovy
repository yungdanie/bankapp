package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Когда вызван метод, он регистрирует пользователя "
    request {
        method POST()
        headers {
            contentType(applicationJson())
        }
        url "/api/signup"
        body([
                      password       : $(consumer(anyNonBlankString()), producer("admin")),
                      confirmPassword : $(consumer(anyNonBlankString()), producer("admin")),
                      name : $(consumer(anyNonBlankString()), producer("admin")),
                      login : $(consumer(anyNonBlankString()), producer("admin")),
                      birthdate : $(consumer(anyNonBlankString()), producer("2000-10-01")),
              ])
    }
    response {
        status 200
    }
}