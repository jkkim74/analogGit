function sankeyCtrl($scope, $http, API_BASE_URL) {

//    $scope.tmpSankeyData =
//    {
//        "nodes":[
//            {"name":"Agricultural 'waste'"},
//            {"name":"Bio-conversion"},
//            {"name":"Liquid"},
//            {"name":"Losses"},
//            {"name":"Solid"},
//            {"name":"Gas"},
//            {"name":"Biofuel imports"},
//            {"name":"Biomass imports"},
//            {"name":"Coal imports"},
//            {"name":"Coal"},
//            {"name":"Coal reserves"},
//            {"name":"District heating"},
//            ],
//        "links":[
//            {"source":0,"target":1,"value":124.729},
//            {"source":1,"target":2,"value":0.597},
//            {"source":1,"target":3,"value":26.862},
//            {"source":1,"target":4,"value":280.322},
//            {"source":1,"target":5,"value":81.144},
//            {"source":6,"target":2,"value":35},
//            {"source":7,"target":4,"value":35},
//            {"source":8,"target":9,"value":11.606},
//            {"source":10,"target":9,"value":63.965},
//            {"source":9,"target":4,"value":75.571}
//        ]
//    };
    $scope.init = function () {
        $scope.drawSankeyChart();
    }

    $scope.drawSankeyChart = function () {
    };
}
