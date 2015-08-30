class UrlMappings {

	static mappings = {

//        name "product" : "/api/product"(controller: "product", parseRequest: true) {
//            action = [GET: "list", POST: "save"]
//            constraints {}
//        }
        "/$controller/$action?/$id?(.${format})?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
