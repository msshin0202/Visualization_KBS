handleSubmit = () => {
  $(document).ready(function() {
    url = $("input.form-input").val();
    $.ajax({
      url: "http://localhost:8080/backend/rest/getDependency",
      type: "POST",
      headers: { 
        "Accept" : "application/json; charset=utf-8",
        "Content-Type": "application/json; charset=utf-8"
      },
      data: JSON.stringify({url: url}),
      dataType:"json"
    })
    .done(res => {
      var existing_svg = $("svg");
      if (existing_svg) {
        existing_svg.remove();
      }

      var width = $(window).width(),
      height = 1000,
      root = res;

      var force = d3.layout.force()
        .size([width, height])
        .linkDistance(120)
        .charge(-300)
        .on("tick", tick);

      var svg = d3.select("body").append("svg")
        .attr("width", width)
        .attr("height", height);

      var link = svg.append("g").selectAll(".link"),
        node = svg.append("g").selectAll(".node"),
        text = svg.append("g").selectAll(".text");


      svg.append("defs").append("marker")
        .attr("id", "arrow")
        .attr("viewBox", "0 -5 10 10")
        .attr("refX", 18)
        .attr("refY", -1.5)
        .attr("markerWidth", 7)
        .attr("markerHeight", 10)
        .attr("orient", "auto")
        .append("path")
        .attr("d", "M0,-5L10,0L0,5")
        .style("stroke", "black");

      svg.append("defs").append("marker")
        .attr("id", "arrow2")
        .attr("viewBox", "0 -5 10 10")
        .attr("refX", 10)
        .attr("refY", 0)
        .attr("markerWidth", 7)
        .attr("markerHeight", 10)
        .attr("orient", "auto")
        .append("path")
        .attr("d", "M0,-5L10,0L0,5")
        .style("stroke", "black");
      
      update();

      function update() {

        var nodes = root,
            links = flatten(root);
        // Restart the force layout.
      
        getSize(root);
        force
            .nodes(nodes)
            .links(links)
            .start();
      
        console.log(root);
      
        // Update the links…
        link = link.data(links);
      
        // Exit any old links.
        link.exit().remove();
      
        // Enter any new links.
        link.enter().append("path")
            .attr("class", "link")
            .attr("marker-end", function(d) {return "url(#arrow"+ (d.weight*0.5 > 2 ? 2 : "")  +")"})
            .style("stroke-width", function(d) {return setMax(d.weight*0.5, 10)});
      
        // Update the nodes…
        node = node.data(nodes).style("fill", color);
      
        // Exit any old nodes.
        node.exit().remove();
      
        // Enter any new nodes.
        node.enter().append("circle")
            .attr("class", "node")
            .attr("cx", function (d) {
                return d.x;
            })
            .attr("cy", function (d) {
                return d.y;
            })
            .attr("r", function (d) {
                return setMax(d.size, 10);
            })
            .style("fill", color)
            .on("click", click)
            .call(force.drag);
      
        text = text.data(nodes);
      
        text.enter().append("text")
            .attr("class", "text")
            .attr("x", 8)
            .attr("y", "0.31em")
            .text(function(d) { return d.className; });
      
      }
      
      function tick() {
        link.attr("d", linkArc);
        link.attr("x1", function (d) {
            return d.source.x;
        })
            .attr("y1", function (d) {
                return d.source.y;
            })
            .attr("x2", function (d) {
                return d.target.x;
            })
            .attr("y2", function (d) {
                return d.target.y;
            });
      
        node.attr("cx", function (d) {
            return d.x;
        })
            .attr("cy", function (d) {
                return d.y;
            });
        text.attr("transform", transform);
      }
      
      function linkArc(d) {
        var dx = d.target.x - d.source.x,
            dy = d.target.y - d.source.y,
            dr = Math.sqrt(dx * dx + dy * dy);
        return "M" + d.source.x + "," + d.source.y + "A" + dr + "," + dr + " 0 0,1 " + d.target.x + "," + d.target.y;
      }
      
      // Color leaf nodes orange, and packages white or blue.
      function color(d) {
        let color = "#3182bd";
        let size = d.size - 4;
        if(size > 12) color = "#0003ff";
        else if(size > 8) color = "#ffff09";
        else if(size > 3) color = "#fd2d3c";
        else if(size > 2) color = "#25ef00";
      
      
      
        return color;
      }
      
      // Toggle children on click.
      function click(d) {
        if (!d3.event.defaultPrevented) {
            if (d.children) {
                d._children = d.children;
                d.children = null;
            } else {
                d.children = d._children;
                d._children = null;
            }
            update();
        }
      }
      
      function getSize(data){
        var nodeMap = {};
      
        for(let i = 0; i < data.length; i++){
            nodeMap[data[i]["className"]] = data[i];
        }
      
        for(let i = 0; i < data.length; i++){
            if (data[i].dependency) {
                for (let j = 0; j < data[i].dependency.length; j++) {
                    let dependency = data[i].dependency[j];
                    nodeMap[dependency["className"]]["size"]++;
                }
            }
        }
      }
      
      function flatten(data) {
        var nodeMap = {};
        var links = [];
      
        for (let i = 0; i < data.length; i++) {
            nodeMap[data[i]["className"]] = i;
            //initial size of node.
            data[i]["size"] = 4;
        }
      
        for (let i = 0; i < data.length; i++) {
            if (data[i].dependency) {
                for (let j = 0; j < data[i].dependency.length; j++) {
                    let link = {source: i, target: nodeMap[data[i].dependency[j]["className"]], weight: data[i].dependency[j]["weight"]};
                    links.push(link);
                }
            }
        }
        return links;
      }
      
      function transform(d) {
        return "translate(" + d.x + "," + d.y + ")";
      }
      
      function setMax(number, max){
        if(number < 1) return 1;
        if(number > max){
            return max;
        } else {
            return number;
        }
      }
      // d3.json("readme2.json", function (error, json) {
      //   if (error) throw error;

      //   root = json;
      //   console.log(root);
      //   update()
      // });

      // function organizeData(data){
      //     var classes = Object.keys(data);
      //     var nodes = [];
      //
      //     for (let i = 0; i < classes.length; i++){
      //         let nodeObject = {};
      //
      //         nodeObject["className"] = classes[i];
      //         nodeObject["dependency"] = [];
      //         let dependencies = Object.keys(data[classes[i]]);
      //         for(let j = 0; j < dependencies.length; j++){
      //             let dependencyObject = {};
      //             dependencyObject["className"] = dependencies[j];
      //             dependencyObject["weight"] = data[classes[i]].dependencies[j];
      //             nodeObject["dependency"].push(dependencyObject);
      //         }
      //         nodes.push(nodeObject);
      //     }
      //     return nodes;
      //
      // }
        })
  })
}