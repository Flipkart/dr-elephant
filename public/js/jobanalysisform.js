/*
 * Copyright 2016 LinkedIn Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

$(document).ready(function(){

  /* Plot graph for data obtained from ajax call */
  $.getJSON('/rest/jobanalysisgraphdata?id=' + queryString()['job-analysis-job-def-id'] + '&finishedTimeBegin=' + queryString()['finished-time-begin'] + '&finishedTimeEnd=' + queryString()['finished-time-end'], function(data) {
    updateExecTimezone(data);
    plotter(data, []);
  });

  loadTableTooltips();
});


/**
 * Example tooltip content:
 *
 * Sat Oct 17 2015 01:47:59 GMT+0530 (IST)
 * Job score = 163672
 * Top poor heuristics
 * Heuristic 1  65%
 * Heuristic 2  25%
 * Heuristic 4  11%
 */
function getGraphTooltipContent(record, jobDefList) {

  var content = document.createElement("div");
  content.style.textAlign = "center";

  var heading = document.createElement("b");
  heading.appendChild(document.createTextNode(record.flowtime));
  heading.appendChild(document.createElement("br"));

  var details = document.createElement("p");
  details.appendChild(document.createTextNode("Job Score = " + record.score));

  var jobTable = document.createElement("table");
  if (record.score != 0) {
    var jobLimit = 3;
    details.appendChild(document.createElement("br"));

    var tableHeader = document.createElement("th");
    tableHeader.setAttribute("colspan", "2");
    tableHeader.style.padding = "3px";
    tableHeader.style.textAlign = "center";
    tableHeader.appendChild(document.createTextNode("Score Distribution"));
    jobTable.appendChild(tableHeader);

    var scoreList = [];
    for (var i = 0; i < record.heuristicInfo.length; i++) {
      var scoreWidth = record.heuristicInfo[i]["heuristicScore"] * 100 / record.score;
      var heuristicName = record.heuristicInfo[i]["heuristicName"];
      scoreList.push([scoreWidth, heuristicName]);
    }

    scoreList.sort(function (left, right) {
      return left[0] > right[0] ? -1 : 1;
    });

    for (var stageIndex = 0; stageIndex < scoreList.length; stageIndex++) {

      var width = scoreList[stageIndex][0];
      var name = scoreList[stageIndex][1];

      // Skip after jobLimit jobs are captured or when width becomes 0.
      if (stageIndex >= jobLimit || width == 0) {
        break;
      }

      var tableCell1 = document.createElement("td");
      tableCell1.style.padding = "3px";
      tableCell1.style.border = "none";
      tableCell1.setAttribute("width", "150px");
      tableCell1.appendChild(document.createTextNode(name));

      var stageScoreRect = document.createElement("div");
      stageScoreRect.style.padding = "3px";
      stageScoreRect.style.background = "red";
      stageScoreRect.style.width = width + "%";
      stageScoreRect.appendChild(document.createTextNode(+width.toFixed(2) + "%"));

      var tableCell2 = document.createElement("td");
      tableCell2.style.border = "none";
      tableCell2.appendChild(stageScoreRect);

      var tableRow = document.createElement("tr");
      tableRow.appendChild(tableCell1);
      tableRow.appendChild(tableCell2);

      jobTable.appendChild(tableRow);
    }

    jobTable.setAttribute("border", "2px solid black");
    jobTable.style.width = "100%";
  }

  content.appendChild(heading);
  content.appendChild(details);
  content.appendChild(jobTable);
  return content;
}