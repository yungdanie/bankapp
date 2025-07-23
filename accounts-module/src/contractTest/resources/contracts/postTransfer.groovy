package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Когда вызван метод, он изменяет баланс пользователя "
    request {
        method POST()
        headers {
            contentType(applicationJson())
        }
        url "/api/transfer"
        body([[
                login       : $(consumer(anyNonBlankString()), producer("admin")),
                currencyCode: $(consumer(anyNonBlankString()), producer("643")),
                amount      : $(anyNumber(), producer(100))
        ]])
    }
    response {
        status 200
    }
}