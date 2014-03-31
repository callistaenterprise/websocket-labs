var stats = {
	type: "GROUP",
contents: {
		
				"spring-mvc-asynch-scenario-089263b6067fd6fb0ad99b059a709ccd": {
		type: "REQUEST",
		name: "spring-mvc-asynch-scenario",
path: "spring-mvc-asynch-scenario",
pathFormatted: "spring-mvc-asynch-scenario-089263b6067fd6fb0ad99b059a709ccd",
stats: {
	numberOfRequests : {
		total: "516420",
		ok: "516420",
		ko: "0"
	},
	minResponseTime : {
		total: "990",
		ok: "990",
		ko: "-"
	},
	maxResponseTime : {
		total: "2220",
		ok: "2220",
		ko: "-"
	},
	meanResponseTime : {
		total: "1502",
		ok: "1502",
		ko: "-"
	},
	standardDeviation : {
		total: "289",
		ok: "289",
		ko: "-"
	},
	percentiles1 : {
		total: "1950",
		ok: "1950",
		ko: "-"
	},
	percentiles2 : {
		total: "1990",
		ok: "1990",
		ko: "-"
	},
	group1 : {
		name: "t < 800 ms",
		count: 0,
		percentage: 0
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 104509,
		percentage: 20
	},
	group3 : {
		name: "t > 1200 ms",
		count: 411911,
		percentage: 79
	},
	group4 : {
		name: "failed",
		count: 0,
		percentage: 0
	},
	meanNumberOfRequestsPerSecond: {
		total: "1174",
		ok: "1174",
		ko: "-"
	}
}


	}
		},
name: "Global Information",
path: "",
pathFormatted: "missing-name",
stats: {
	numberOfRequests : {
		total: "516420",
		ok: "516420",
		ko: "0"
	},
	minResponseTime : {
		total: "990",
		ok: "990",
		ko: "-"
	},
	maxResponseTime : {
		total: "2220",
		ok: "2220",
		ko: "-"
	},
	meanResponseTime : {
		total: "1502",
		ok: "1502",
		ko: "-"
	},
	standardDeviation : {
		total: "289",
		ok: "289",
		ko: "-"
	},
	percentiles1 : {
		total: "1950",
		ok: "1950",
		ko: "-"
	},
	percentiles2 : {
		total: "1990",
		ok: "1990",
		ko: "-"
	},
	group1 : {
		name: "t < 800 ms",
		count: 0,
		percentage: 0
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 104509,
		percentage: 20
	},
	group3 : {
		name: "t > 1200 ms",
		count: 411911,
		percentage: 79
	},
	group4 : {
		name: "failed",
		count: 0,
		percentage: 0
	},
	meanNumberOfRequestsPerSecond: {
		total: "1174",
		ok: "1174",
		ko: "-"
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
