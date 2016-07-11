function chart(selection) {
    selection.each(function (data) {
        var container = d3.select(this),
            that = this;

        var availableWidth = (width || parseInt(container.style('width')) || 960)
                - margin.left - margin.right,
            availableHeight = (height || parseInt(container.style('height')) || 400)
                - margin.top - margin.bottom;

        chart.update = function () {
            container.transition().duration(transitionDuration).call(chart);
        };
        chart.container = this;

        //set state.disabled
        state.disabled = data.map(function (d) {
            return !!d.disabled
        });

        if (!defaultState) {
            var key;
            defaultState = {};
            for (key in state) {
                if (state[key] instanceof Array)
                    defaultState[key] = state[key].slice(0);
                else
                    defaultState[key] = state[key];
            }
        }

        //------------------------------------------------------------
        // Display No Data message if there's nothing to show.

        if (!data || !data.length || !data.filter(function (d) {
                return d.values.length
            }).length) {
            var noDataText = container.selectAll('.nv-noData').data([noData]);

            noDataText.enter().append('text')
                .attr('class', 'nvd3 nv-noData')
                .attr('dy', '-.7em')
                .style('text-anchor', 'middle');

            noDataText
                .attr('x', margin.left + availableWidth / 2)
                .attr('y', margin.top + availableHeight / 2)
                .text(function (d) {
                    return d
                });

            return chart;
        } else {
            container.selectAll('.nv-noData').remove();
        }

        //------------------------------------------------------------


        //------------------------------------------------------------
        // Setup Scales

        x = stacked.xScale();
        y = stacked.yScale();

        //------------------------------------------------------------


        //------------------------------------------------------------
        // Setup containers and skeleton of chart

        var wrap = container.selectAll('g.nv-wrap.nv-stackedAreaChart').data([data]);
        var gEnter = wrap.enter().append('g').attr('class', 'nvd3 nv-wrap nv-stackedAreaChart').append('g');
        var g = wrap.select('g');

        gEnter.append("rect").style("opacity", 0);
        gEnter.append('g').attr('class', 'nv-x nv-axis');
        gEnter.append('g').attr('class', 'nv-y nv-axis');
        gEnter.append('g').attr('class', 'nv-stackedWrap');
        gEnter.append('g').attr('class', 'nv-legendWrap');
        gEnter.append('g').attr('class', 'nv-controlsWrap');
        gEnter.append('g').attr('class', 'nv-interactive');

        g.select("rect").attr("width", availableWidth).attr("height", availableHeight);
        //------------------------------------------------------------
        // Legend

        if (showLegend) {
            var legendWidth = (showControls) ? availableWidth - controlWidth : availableWidth;
            legend
                .width(legendWidth);

            g.select('.nv-legendWrap')
                .datum(data)
                .call(legend);

            if (margin.top != legend.height()) {
                margin.top = legend.height();
                availableHeight = (height || parseInt(container.style('height')) || 400)
                - margin.top - margin.bottom;
            }

            g.select('.nv-legendWrap')
                .attr('transform', 'translate(' + (availableWidth - legendWidth) + ',' + (-margin.top) + ')');
        }

        //------------------------------------------------------------


        //------------------------------------------------------------
        // Controls

        if (showControls) {
            var controlsData = [
                {
                    key: controlLabels.stacked || 'Stacked',
                    metaKey: 'Stacked',
                    disabled: stacked.style() != 'stack',
                    style: 'stack'
                },
                {
                    key: controlLabels.stream || 'Stream',
                    metaKey: 'Stream',
                    disabled: stacked.style() != 'stream',
                    style: 'stream'
                },
                {
                    key: controlLabels.expanded || 'Expanded',
                    metaKey: 'Expanded',
                    disabled: stacked.style() != 'expand',
                    style: 'expand'
                },
                {
                    key: controlLabels.stack_percent || 'Stack %',
                    metaKey: 'Stack_Percent',
                    disabled: stacked.style() != 'stack_percent',
                    style: 'stack_percent'
                }
            ];

            controlWidth = (cData.length / 3) * 260;

            controlsData = controlsData.filter(function (d) {
                return cData.indexOf(d.metaKey) !== -1;
            })

            controls
                .width(controlWidth)
                .color(['#444', '#444', '#444']);

            g.select('.nv-controlsWrap')
                .datum(controlsData)
                .call(controls);


            if (margin.top != Math.max(controls.height(), legend.height())) {
                margin.top = Math.max(controls.height(), legend.height());
                availableHeight = (height || parseInt(container.style('height')) || 400)
                - margin.top - margin.bottom;
            }


            g.select('.nv-controlsWrap')
                .attr('transform', 'translate(0,' + (-margin.top) + ')');
        }

        //------------------------------------------------------------


        wrap.attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');

        if (rightAlignYAxis) {
            g.select(".nv-y.nv-axis")
                .attr("transform", "translate(" + availableWidth + ",0)");
        }

        //------------------------------------------------------------
        // Main Chart Component(s)

        //------------------------------------------------------------
        //Set up interactive layer
        if (useInteractiveGuideline) {
            interactiveLayer
                .width(availableWidth)
                .height(availableHeight)
                .margin({left: margin.left, top: margin.top})
                .svgContainer(container)
                .xScale(x);
            wrap.select(".nv-interactive").call(interactiveLayer);
        }

        stacked
            .width(availableWidth)
            .height(availableHeight)

        var stackedWrap = g.select('.nv-stackedWrap')
            .datum(data);

        stackedWrap.transition().call(stacked);

        //------------------------------------------------------------


        //------------------------------------------------------------
        // Setup Axes

        if (showXAxis) {
            xAxis
                .scale(x)
                .ticks(availableWidth / 100)
                .tickSize(-availableHeight, 0);

            g.select('.nv-x.nv-axis')
                .attr('transform', 'translate(0,' + availableHeight + ')');

            g.select('.nv-x.nv-axis')
                .transition().duration(0)
                .call(xAxis);
        }

        if (showYAxis) {
            yAxis
                .scale(y)
                .ticks(stacked.offset() == 'wiggle' ? 0 : availableHeight / 36)
                .tickSize(-availableWidth, 0)
                .setTickFormat((stacked.style() == 'expand' || stacked.style() == 'stack_percent')
                    ? d3.format('%') : yAxisTickFormat);

            g.select('.nv-y.nv-axis')
                .transition().duration(0)
                .call(yAxis);
        }

        //------------------------------------------------------------


        //============================================================
        // Event Handling/Dispatching (in chart's scope)
        //------------------------------------------------------------

        stacked.dispatch.on('areaClick.toggle', function (e) {
            if (data.filter(function (d) {
                    return !d.disabled
                }).length === 1)
                data.forEach(function (d) {
                    d.disabled = false;
                });
            else
                data.forEach(function (d, i) {
                    d.disabled = (i != e.seriesIndex);
                });

            state.disabled = data.map(function (d) {
                return !!d.disabled
            });
            dispatch.stateChange(state);

            chart.update();
        });

        legend.dispatch.on('stateChange', function (newState) {
            state.disabled = newState.disabled;
            dispatch.stateChange(state);
            chart.update();
        });

        controls.dispatch.on('legendClick', function (d, i) {
            if (!d.disabled) return;

            controlsData = controlsData.map(function (s) {
                s.disabled = true;
                return s;
            });
            d.disabled = false;

            stacked.style(d.style);


            state.style = stacked.style();
            dispatch.stateChange(state);

            chart.update();
        });


        interactiveLayer.dispatch.on('elementMousemove', function (e) {
            stacked.clearHighlights();
            var singlePoint, pointIndex, pointXLocation, allData = [];
            data
                .filter(function (series, i) {
                    series.seriesIndex = i;
                    return !series.disabled;
                })
                .forEach(function (series, i) {
                    pointIndex = nv.interactiveBisect(series.values, e.pointXValue, chart.x());
                    stacked.highlightPoint(i, pointIndex, true);
                    var point = series.values[pointIndex];
                    if (typeof point === 'undefined') return;
                    if (typeof singlePoint === 'undefined') singlePoint = point;
                    if (typeof pointXLocation === 'undefined') pointXLocation = chart.xScale()(chart.x()(point, pointIndex));

                    //If we are in 'expand' mode, use the stacked percent value instead of raw value.
                    var tooltipValue = (stacked.style() == 'expand') ? point.display.y : chart.y()(point, pointIndex);
                    allData.push({
                        key: series.key,
                        value: tooltipValue,
                        color: color(series, series.seriesIndex),
                        stackedValue: point.display
                    });
                });

            allData.reverse();

            //Highlight the tooltip entry based on which stack the mouse is closest to.
            if (allData.length > 2) {
                var yValue = chart.yScale().invert(e.mouseY);
                var yDistMax = Infinity, indexToHighlight = null;
                allData.forEach(function (series, i) {

                    //To handle situation where the stacked area chart is negative, we need to use absolute values
                    //when checking if the mouse Y value is within the stack area.
                    yValue = Math.abs(yValue);
                    var stackedY0 = Math.abs(series.stackedValue.y0);
                    var stackedY = Math.abs(series.stackedValue.y);
                    if (yValue >= stackedY0 && yValue <= (stackedY + stackedY0)) {
                        indexToHighlight = i;
                        return;
                    }
                });
                if (indexToHighlight != null)
                    allData[indexToHighlight].highlight = true;
            }

            var xValue = xAxis.tickFormat()(chart.x()(singlePoint, pointIndex));

            //If we are in 'expand' mode, force the format to be a percentage.
            var valueFormatter = (stacked.style() == 'expand') ?
                function (d, i) {
                    return d3.format(".1%")(d);
                } :
                function (d, i) {
                    return yAxis.tickFormat()(d);
                };
            interactiveLayer.tooltip
                .position({left: pointXLocation + margin.left, top: e.mouseY + margin.top})
                .chartContainer(that.parentNode)
                .enabled(tooltips)
                .valueFormatter(valueFormatter)
                .data(
                {
                    value: xValue,
                    series: allData
                }
            )();

            interactiveLayer.renderGuideLine(pointXLocation);

        });

        interactiveLayer.dispatch.on("elementMouseout", function (e) {
            dispatch.tooltipHide();
            stacked.clearHighlights();
        });


        dispatch.on('tooltipShow', function (e) {
            if (tooltips) showTooltip(e, that.parentNode);
        });

        // Update chart from a state object passed to event handler
        dispatch.on('changeState', function (e) {

            if (typeof e.disabled !== 'undefined' && data.length === e.disabled.length) {
                data.forEach(function (series, i) {
                    series.disabled = e.disabled[i];
                });

                state.disabled = e.disabled;
            }

            if (typeof e.style !== 'undefined') {
                stacked.style(e.style);
            }

            chart.update();
        });

    });


    return chart;
}