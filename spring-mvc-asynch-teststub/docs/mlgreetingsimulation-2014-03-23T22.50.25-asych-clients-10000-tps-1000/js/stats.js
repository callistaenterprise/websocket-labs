var stats = {
	type: "GROUP",
contents: {
		
				"test-ping-30a7416e5c21f39bb391199f6c121f24": {
		type: "REQUEST",
		name: "test-ping",
path: "test-ping",
pathFormatted: "test-ping-30a7416e5c21f39bb391199f6c121f24",
stats: {
	numberOfRequests : {
		total: "176456",
		ok: "176456",
		ko: "0"
	},
	minResponseTime : {
		total: "4960",
		ok: "4960",
		ko: "-"
	},
	maxResponseTime : {
		total: "8390",
		ok: "8390",
		ko: "-"
	},
	meanResponseTime : {
		total: "6503",
		ok: "6503",
		ko: "-"
	},
	standardDeviation : {
		total: "868",
		ok: "868",
		ko: "-"
	},
	percentiles1 : {
		total: "7850",
		ok: "7850",
		ko: "-"
	},
	percentiles2 : {
		total: "7970",
		ok: "7970",
		ko: "-"
	},
	group1 : {
		name: "t < 800 ms",
		count: 0,
		percentage: 0
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 0,
		percentage: 0
	},
	group3 : {
		name: "t > 1200 ms",
		count: 176456,
		percentage: 100
	},
	group4 : {
		name: "failed",
		count: 0,
		percentage: 0
	},
	meanNumberOfRequestsPerSecond: {
		total: "666",
		ok: "666",
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
		total: "176456",
		ok: "176456",
		ko: "0"
	},
	minResponseTime : {
		total: "4960",
		ok: "4960",
		ko: "-"
	},
	maxResponseTime : {
		total: "8390",
		ok: "8390",
		ko: "-"
	},
	meanResponseTime : {
		total: "6503",
		ok: "6503",
		ko: "-"
	},
	standardDeviation : {
		total: "868",
		ok: "868",
		ko: "-"
	},
	percentiles1 : {
		total: "7850",
		ok: "7850",
		ko: "-"
	},
	percentiles2 : {
		total: "7970",
		ok: "7970",
		ko: "-"
	},
	group1 : {
		name: "t < 800 ms",
		count: 0,
		percentage: 0
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 0,
		percentage: 0
	},
	group3 : {
		name: "t > 1200 ms",
		count: 176456,
		percentage: 100
	},
	group4 : {
		name: "failed",
		count: 0,
		percentage: 0
	},
	meanNumberOfRequestsPerSecond: {
		total: "666",
		ok: "666",
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
