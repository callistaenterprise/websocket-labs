var stats = {
	type: "GROUP",
contents: {
		
				"spring-mvc-asynch-teststub-request-9a6ad9ee0cb7e26ea5b5c3b22c66f7c5": {
		type: "REQUEST",
		name: "spring-mvc-asynch-teststub-request",
path: "spring-mvc-asynch-teststub-request",
pathFormatted: "spring-mvc-asynch-teststub-request-9a6ad9ee0cb7e26ea5b5c3b22c66f7c5",
stats: {
	numberOfRequests : {
		total: "36159",
		ok: "20694",
		ko: "15465"
	},
	minResponseTime : {
		total: "0",
		ok: "1000",
		ko: "0"
	},
	maxResponseTime : {
		total: "10090",
		ok: "10000",
		ko: "10090"
	},
	meanResponseTime : {
		total: "6926",
		ok: "4892",
		ko: "9648"
	},
	standardDeviation : {
		total: "3389",
		ok: "2803",
		ko: "1844"
	},
	percentiles1 : {
		total: "10000",
		ok: "9500",
		ko: "10000"
	},
	percentiles2 : {
		total: "10000",
		ok: "9910",
		ko: "10010"
	},
	group1 : {
		name: "t < 800 ms",
		count: 0,
		percentage: 0
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 802,
		percentage: 2
	},
	group3 : {
		name: "t > 1200 ms",
		count: 19892,
		percentage: 55
	},
	group4 : {
		name: "failed",
		count: 15465,
		percentage: 42
	},
	meanNumberOfRequestsPerSecond: {
		total: "257",
		ok: "147",
		ko: "110"
	}
}


	}
		},
name: "Global Information",
path: "",
pathFormatted: "missing-name",
stats: {
	numberOfRequests : {
		total: "36159",
		ok: "20694",
		ko: "15465"
	},
	minResponseTime : {
		total: "0",
		ok: "1000",
		ko: "0"
	},
	maxResponseTime : {
		total: "10090",
		ok: "10000",
		ko: "10090"
	},
	meanResponseTime : {
		total: "6926",
		ok: "4892",
		ko: "9648"
	},
	standardDeviation : {
		total: "3389",
		ok: "2803",
		ko: "1844"
	},
	percentiles1 : {
		total: "10000",
		ok: "9500",
		ko: "10000"
	},
	percentiles2 : {
		total: "10000",
		ok: "9910",
		ko: "10010"
	},
	group1 : {
		name: "t < 800 ms",
		count: 0,
		percentage: 0
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 802,
		percentage: 2
	},
	group3 : {
		name: "t > 1200 ms",
		count: 19892,
		percentage: 55
	},
	group4 : {
		name: "failed",
		count: 15465,
		percentage: 42
	},
	meanNumberOfRequestsPerSecond: {
		total: "257",
		ok: "147",
		ko: "110"
	}
}



}

function fillStats(stat){
    $("#numberOfRequests").append(stat.numberOfRequests.total);
    $("#numberOfRequestsOK").append(stat.numberOfRequests.ok);
    $("#numberOfRequestsKO").append(stat.numberOfRequests.ko);

    $("#minResponseTime").append(stat.minResponseTime.total);
    $("#minResponseTimeOK").append(stat.minResponseTime.ok);
    $("#minResponseTimeKO").append(stat.minResponseTime.ko);

    $("#maxResponseTime").append(stat.maxResponseTime.total);
    $("#maxResponseTimeOK").append(stat.maxResponseTime.ok);
    $("#maxResponseTimeKO").append(stat.maxResponseTime.ko);

    $("#meanResponseTime").append(stat.meanResponseTime.total);
    $("#meanResponseTimeOK").append(stat.meanResponseTime.ok);
    $("#meanResponseTimeKO").append(stat.meanResponseTime.ko);

    $("#standardDeviation").append(stat.standardDeviation.total);
    $("#standardDeviationOK").append(stat.standardDeviation.ok);
    $("#standardDeviationKO").append(stat.standardDeviation.ko);

    $("#percentiles1").append(stat.percentiles1.total);
    $("#percentiles1OK").append(stat.percentiles1.ok);
    $("#percentiles1KO").append(stat.percentiles1.ko);

    $("#percentiles2").append(stat.percentiles2.total);
    $("#percentiles2OK").append(stat.percentiles2.ok);
    $("#percentiles2KO").append(stat.percentiles2.ko);

    $("#meanNumberOfRequestsPerSecond").append(stat.meanNumberOfRequestsPerSecond.total);
    $("#meanNumberOfRequestsPerSecondOK").append(stat.meanNumberOfRequestsPerSecond.ok);
    $("#meanNumberOfRequestsPerSecondKO").append(stat.meanNumberOfRequestsPerSecond.ko);
}
