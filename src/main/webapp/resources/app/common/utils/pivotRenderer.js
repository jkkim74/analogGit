var pivotTableRendererForMergingTableHeader = function (pivotData, opts) {
    var aggregator, c, colAttrs, colKey, colKeys, defaults, i, j, r, result, rowAttrs, rowKey, rowKeys, td, th, prevTh, totalAggregator, tr, txt, val, x, __hasProp, spanSize, setPos, convertByDictionary;
    __hasProp = {}.hasOwnProperty;
    setPos = function (el, row, col) {
        el.setAttribute('data-col', col);
    };
    spanSize = function (arr, i, j) {
        var len, noDraw, stop, x, _i, _j;
        if (i !== 0) {
            noDraw = true;
            for (x = _i = 0; 0 <= j ? _i <= j : _i >= j; x = 0 <= j ? ++_i : --_i) {
                if (arr[i - 1][x] !== arr[i][x]) {
                    noDraw = false;
                }
            }
            if (noDraw) {
                return -1;
            }        }
        len = 0;
        while (i + len < arr.length) {
            stop = false;

            for (x = _j = 0; 0 <= j ? _j <= j : _j >= j; x = 0 <= j ? ++_j : --_j) {
                if (arr[i][x] !== arr[i + len][x]) {
                    stop = true;
                }
            }
            if (stop) {
                break;
            }
            len++;
        }
        return len;
    };
    // 사용자 정의 문구로 변경 함수
    convertByDictionary = function (word) {
        if (!opts.dictionary) return word;
        return opts.dictionary[word] || word;
    };

    defaults = {
        localeStrings: {
            totals: "합계"
        }
    };
    opts = $.extend(defaults, opts);
    colAttrs = pivotData.colAttrs;
    rowAttrs = pivotData.rowAttrs;
    rowKeys = pivotData.getRowKeys();
    colKeys = pivotData.getColKeys();
    result = document.createElement("table");
    result.className = "multi-header-pivot";
    // 총 사용되는 컬럼 수를 넣어준다.
    result.setAttribute('data-totalCol', rowAttrs.length + colKeys.length + 2);
    for (j in colAttrs) {
        if (!__hasProp.call(colAttrs, j)) continue;
        c = colAttrs[j];
        tr = document.createElement("tr");
        tr.className = 'pivot-header';
        if (parseInt(j) === 0 && rowAttrs.length !== 0) {
            th = document.createElement("th");
            setPos(th, 0, 0);
            th.setAttribute("colspan", rowAttrs.length);
            th.setAttribute("rowspan", colAttrs.length);
            tr.appendChild(th);
        }
        th = document.createElement("th");
        th.className = "pvtAxisLabel";
        setPos(th, j, rowAttrs.length);
        th.textContent = convertByDictionary(c);
        tr.appendChild(th);
        for (i in colKeys) {
            if (!__hasProp.call(colKeys, i)) continue;
            colKey = colKeys[i];
            x = spanSize(colKeys, parseInt(i), parseInt(j));
            if (x !== -1) {
                th = document.createElement("th");
                th.className = "pvtColLabel";
                // +1을 하는 이유는 0,0 위치의 element가 collection에 포함된 속성이 아니기 때문이다.
                setPos(th, j, parseInt(i) + rowAttrs.length + 1);
                th.textContent = colKey[j];
                th.setAttribute("colspan", x);
                if (parseInt(j) === colAttrs.length - 1 && rowAttrs.length !== 0) {
                    th.setAttribute("rowspan", 2);
                }
                tr.appendChild(th);
            }
        }
        // 가로 합계 레이블
        if (parseInt(j) === 0) {
            th = document.createElement("th");
            th.className = "pivotTotalLabel";
            // +1을 하는 이유는 pivotTotalLabel이 collection에 포함된 속성이 아니기 때문이다.
            setPos(th, j, parseInt(i) + rowAttrs.length + 1 + 1);
            th.textContent = opts.localeStrings.totals;
            th.setAttribute("rowspan", colAttrs.length + (rowAttrs.length === 0 ? 0 : 1));
            tr.appendChild(th);
        }
        result.appendChild(tr);
    }

    if (rowAttrs.length !== 0) {
        tr = document.createElement("tr");
        tr.className = 'pivot-header';
        for (i in rowAttrs) {
            if (!__hasProp.call(rowAttrs, i)) continue;
            r = rowAttrs[i];
            th = document.createElement("th");
            th.className = "pvtAxisLabel";
            setPos(th, colAttrs.length, parseInt(i));
            th.textContent = convertByDictionary(r);
            tr.appendChild(th);
        }
        th = document.createElement("th");
        setPos(th, colAttrs.length, parseInt(i) + 1);
        if (colAttrs.length === 0) {
            th.className = "pivotTotalLabel";
            th.textContent = opts.localeStrings.totals;
        }

        tr.appendChild(th);
        result.appendChild(tr);
    }

    for (i in rowKeys) {
        if (!__hasProp.call(rowKeys, i)) continue;
        rowKey = rowKeys[i];
        tr = document.createElement("tr");
        for (j in rowKey) {
            if (!__hasProp.call(rowKey, j)) continue;
            txt = rowKey[j];
            x = spanSize(rowKeys, parseInt(i), parseInt(j));
            if (x !== -1) {
                if (txt === '' && x === parseInt(prevTh.getAttribute('rowSpan'))) {
                    if (!prevTh.getAttribute('colspan')) {
                        prevTh.setAttribute('colspan', 2);
                    } else {
                        prevTh.setAttribute('colspan', parseInt(prevTh.getAttribute('colspan')) + 1);
                    }

                    if (parseInt(j) === rowAttrs.length - 1 && colAttrs.length !== 0) {
                        prevTh.setAttribute('colspan', parseInt(prevTh.getAttribute('colspan')) + 1);
                    }
                    continue;
                }
                th = document.createElement("th");
                th.className = "pvtRowLabel";
                th.textContent = txt;
                th.textContent = th.textContent.replace(/.*?@@/, '');
                th.textContent = convertByDictionary(th.textContent);
                th.setAttribute("rowspan", x);
                setPos(th, parseInt(i) + colAttrs.length + 1, j);
                if (parseInt(j) === rowAttrs.length - 1 && colAttrs.length !== 0) {
                    th.setAttribute("colspan", 2);
                }
                prevTh = th;
                tr.appendChild(th);
            }
        }

        for (j in colKeys) {
            if (!__hasProp.call(colKeys, j)) continue;
            colKey = colKeys[j];
            aggregator = pivotData.getAggregator(rowKey, colKey);
            val = aggregator.value();
            td = document.createElement("td");
            td.className = "pvtVal row" + i + " col" + j;
            setPos(td, parseInt(i) + colAttrs.length + 1, parseInt(j) + rowAttrs.length + 1);
            td.textContent = aggregator.format(val);
            td.setAttribute("data-value", val);
            tr.appendChild(td);
        }
        // 가로 합계 추출 부분
        totalAggregator = pivotData.getAggregator(rowKey, []);
        val = totalAggregator.value();
        td = document.createElement("td");
        td.className = "pivotTotal rowTotal";
        setPos(td, parseInt(i) + colAttrs.length + 1, parseInt(j) + rowAttrs.length + 1 + 1);
        td.textContent = totalAggregator.format(val);
        td.setAttribute("data-value", val);
        td.setAttribute("data-for", "row" + i);
        tr.appendChild(td);
        result.appendChild(tr);
    }

    if ($.isFunction(opts.callback)) {
        opts.callback();
    }
    return result;
};