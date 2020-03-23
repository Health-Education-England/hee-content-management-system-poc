/*!
 * Copyright 2014-2015 Hippo B.V. (http://www.onehippo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
(function () {
  "use strict";

  angular.module('hippo.theme', [
    'ngAria',
    'ui.bootstrap',
    'ui.tree',
    'localytics.directives',
    'hippo-theme.templates'
  ]);
})();

angular.module("hippo-theme.templates", ["hippo-icon/hippo-icon.tpl.html", "tree/tree-include.tpl.html", "tree/tree.tpl.html"]);

angular.module("hippo-icon/hippo-icon.tpl.html", []).run(["$templateCache", function($templateCache) {
  "use strict";
  $templateCache.put("hippo-icon/hippo-icon.tpl.html",
    "<svg ng-class=\"className\"><use xlink:href=\"{{xlink}}\"/></svg>");
}]);

angular.module("tree/tree-include.tpl.html", []).run(["$templateCache", function($templateCache) {
  "use strict";
  $templateCache.put("tree/tree-include.tpl.html",
    "<div ui-tree-handle data-ng-click=\"selectItem()\"><div hippo-tree-template></div></div><ol ui-tree-nodes ng-model=\"item.items\" ng-if=\"!collapsed\"><li ng-repeat=\"item in item.items\" ui-tree-node data-hippo-node-template-url=\"tree/tree-include.tpl.html\" ng-if=\"displayTreeItem(item)\" data-ng-class=\"{active: selectedItem.id == item.id}\" scroll-to-if=\"selectedItem.id == item.id\" data-collapsed=\"item.collapsed\"></li></ol>");
}]);

angular.module("tree/tree.tpl.html", []).run(["$templateCache", function($templateCache) {
  "use strict";
  $templateCache.put("tree/tree.tpl.html",
    "<div ui-tree=\"options\" ng-class=\"{\n" +
    "       'drag-enabled': drag == true,\n" +
    "       'drag-disabled': drag == false,\n" +
    "     }\" data-drag-enabled=\"drag\"><ol ui-tree-nodes ng-model=\"treeItems\"><li ng-repeat=\"item in treeItems\" ui-tree-node data-hippo-node-template-url=\"tree/tree-include.tpl.html\" data-ng-class=\"{active: selectedItem.id == item.id}\" scroll-to-if=\"selectedItem.id == item.id\" data-collapsed=\"item.collapsed\"></li></ol></div>");
}]);

/*
 * Copyright 2014-2015 Hippo B.V. (http://www.onehippo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

(function () {
  "use strict";

  angular.module('hippo.theme')
  /**
   * @ngdoc directive
   * @name hippo.theme.directive:a
   * @restrict E
   *
   * @description
   * Blurs an anchor when it is clicked. This removes the default outline for clicked anchors in multiple browsers.
   */
    .directive('a', function () {
      return {
        restrict: 'E',
        link: function (scope, element) {
          element.on('click', function () {
            element.blur();
          });
        }
      };
    })

  /**
   * @ngdoc directive
   * @name hippo.theme.directive:button
   * @restrict E
   *
   * @description
   * Blurs a button when it is clicked. This removes the default outline for clicked buttons in multiple browsers.
   */
    .directive('button', function () {
      return {
        restrict: 'E',
        link: function (scope, element) {
          element.on('click', function () {
            element.blur();
          });
        }
      };
    });

}());

/*
 * Copyright 2015 Hippo B.V. (http://www.onehippo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
(function () {
  "use strict";

  angular.module('hippo.theme')
  /**
   * @ngdoc filter
   * @name hippo.theme.directive:hippoGetByProperty
   *
   * @description
   * Return the object in a collection that has a property that matches the value passed in
   */
    .filter('hippoGetByProperty', function () {
      return function (collection, propertyName, propertyValue, subCollection) {
        var itemWithProperty;

        function findPropertiesAndSubProperties (newCollection) {
          for (var i = 0; i < newCollection.length; i++) {
            if (newCollection[i][propertyName] === propertyValue) {
              itemWithProperty = newCollection[i];
            }
            if (subCollection && newCollection[i][subCollection]) {
              findPropertiesAndSubProperties(newCollection[i][subCollection]);
            }
          }
        }

        findPropertiesAndSubProperties(collection);

        return itemWithProperty;
      };
    });
}());

/*
 * Copyright 2015 Hippo B.V. (http://www.onehippo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

(function () {
  'use strict';

  angular.module('hippo.theme')
    /**
     * @ngdoc directive
     * @name hippo.theme.directive:hippoIcon
     *
     * @description
     * Hippo Icon will generate a svg icon based on the values passed in of name, position and size.
     * See the hippo theme demo to see the different options and combinations
     *
     * @param {String} name The name of the icon
     * @param {String} position The position of the icon
     * @param {String} size The size of the icon
     *
     */
    .directive('hippoIcon', [
      function () {
        return {
          replace: true,
          restrict: 'E',
          scope: {
            name: '@',
            position: '@',
            size: '@'
          },
          templateUrl: 'hippo-icon/hippo-icon.tpl.html',
          link: function (scope) {
            var xlink,
              iconPosition = '',
              iconName = '',
              defaultSize = 'm';

            if (scope.position) {
              angular.forEach(scope.position.split(' '), function (position, i) {
                if (position === 'center') {
                  if (i === 0) {
                    position = 'vcenter';
                  } else {
                    position = 'hcenter';
                  }
                }

                iconPosition += ' hi-' + position;
              });
            }

            scope.$watchGroup(['name', 'size'], function () {
              if (scope.size) {
                iconName = ' hi-' + scope.name + ' hi-' + scope.size;
                xlink = '#hi-' + scope.name + '-' + scope.size;
              } else {
                iconName += ' hi-' + scope.name + ' hi-' + defaultSize;
                xlink = '#hi-' + scope.name + '-' + defaultSize;
              }

              scope.className = 'hi' + iconPosition + iconName;
              scope.xlink = xlink;
            });
          }
        };
      }
    ]
  );
})();

/*
 * Copyright 2014-2015 Hippo B.V. (http://www.onehippo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
(function () {
  'use strict';

  angular.module('hippo.theme')
  /**
   * @ngdoc directive
   * @name hippo.theme.directive:scrollToIf
   *
   * @description
   * Directive to scroll to an item if an expression evaluates to true.
   */
    .directive('scrollToIf', [
      '$timeout',
      function ($timeout) {
        var getParentOfScrollItem = function (element) {
          element = element.parentElement;
          while (element) {
            if (element.scrollHeight !== element.clientHeight) {
              return element;
            }
            if (element.parentElement) {
              element = element.parentElement;
            } else {
              return element;
            }
          }
          return null;
        };
        return function (scope, element, attrs) {
          scope.$watch(attrs.scrollToIf, function (value) {
            if (value) {
              $timeout(function () {
                var parent = getParentOfScrollItem(element[0]),
                  topPadding = parseInt(window.getComputedStyle(parent, null).getPropertyValue('padding-top')) || 0,
                  leftPadding = parseInt(window.getComputedStyle(parent, null).getPropertyValue('padding-left')) || 0,
                  elemOffsetTop = element[0].offsetTop,
                  elemOffsetLeft = element[0].offsetLeft,
                  elemHeight = element[0].clientHeight,
                  elemWidth = element[0].clientWidth;

                if (elemOffsetTop < parent.scrollTop) {
                  parent.scrollTop = elemOffsetTop + topPadding;
                } else if (elemOffsetTop + elemHeight > parent.scrollTop + parent.clientHeight) {
                  if (elemHeight > parent.clientHeight) {
                    elemHeight = elemHeight - (elemHeight - parent.clientHeight);
                  }
                  parent.scrollTop = elemOffsetTop + topPadding + elemHeight - parent.clientHeight;
                }
                if (elemOffsetLeft + elemWidth > parent.scrollLeft + parent.clientWidth) {
                  parent.scrollLeft = elemOffsetLeft + leftPadding;
                }
              }, 0);
            }
          });
        };
      }
    ]
  );
}());

/*
 * Copyright 2014 Hippo B.V. (http://www.onehippo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
(function () {
  "use strict";

  angular.module('hippo.theme')
  /**
   * @ngdoc directive
   * @name hippo.theme.directive:stopPropagation
   * @restrict A
   *
   * @description
   * Prevent event bubbling
   */
    .directive('stopPropagation', function () {
      return {
        restrict: 'A',
        link: function (scope, element) {
          element.bind('click', function (e) {
            e.stopPropagation();
          });
        }
      };
    });
}());

/*
 * Copyright 2014-2015 Hippo B.V. (http://www.onehippo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
(function () {
  'use strict';

  angular.module('hippo.theme')

  /**
   * @ngdoc directive
   * @name hippo.theme.directive:hippoTree
   * @restrict A
   *
   * @description
   * Tree component for the Hippo Theme based on [NestedSortable](https://github.com/JimLiu/Angular-NestedSortable).
   *
   * @param {Array} items The items to use for the Tree. Each item is an object with `title` (String) and `items`
   *   (Array) property.
   * @param {Object} selectedItem The item that should be marked as selected.
   * @param {callbacks} callbacks The available callbacks. A list of all available callbacks is available at
   * the [Hippo Theme demo](http://onehippo.github.io/hippo-theme-demo/) page.
   */
    .directive('hippoTree', function () {
      return {
        restrict: 'A',
        transclude: true,
        scope: {
          drag: '=',
          options: '=callbacks',
          selectedItem: '=',
          treeItems: '=items'
        },
        templateUrl: 'tree/tree.tpl.html',
        controller: 'hippo.theme.tree.TreeCtrl'
      };
    })

    .controller('hippo.theme.tree.TreeCtrl', [
      '$transclude',
      '$scope',
      '$filter',
      function ($transclude, $scope, $filter) {
        this.renderTreeTemplate = $transclude;

        function collectNodes (items, map) {
          angular.forEach(items, function (item) {
            map[item.id] = item;
            collectNodes(item.items, map);
          });
          return map;
        }

        function copyCollapsedState (srcNodes, targetNodes) {
          angular.forEach(srcNodes, function (srcNode) {
            var targetNode = targetNodes[srcNode.id];
            if (targetNode) {
              targetNode.collapsed = srcNode.collapsed;
            }
          });
        }

        $scope.toggleItem = function () {
          var item = this.$modelValue;
          this.toggle();
          item.collapsed = !item.collapsed;

          if (item.collapsed && $filter('hippoGetByProperty')(item.items, 'id', $scope.selectedItem.id, 'items')) {
            $scope.selectItem(item);
          }
          if (angular.isFunction($scope.options.toggleItem)) {
            $scope.options.toggleItem(item);
          }
        };

        $scope.selectItem = function (item) {
          if (!item) {
            item = this.$modelValue;
          }
          $scope.selectedItem = item;
          if (angular.isFunction($scope.options.selectItem)) {
            $scope.options.selectItem(item);
          }
        };

        $scope.displayTreeItem = function (item) {
          if (angular.isFunction($scope.options.displayTreeItem)) {
            return $scope.options.displayTreeItem(item);
          } else {
            return true;
          }
        };

        $scope.$watch('treeItems', function (newItems, oldItems) {
          var oldNodes = collectNodes(oldItems, {}),
            newNodes = collectNodes(newItems, {});
          copyCollapsedState(oldNodes, newNodes);
        });
      }
    ])

    .directive('hippoTreeTemplate', function () {
      return {
        require: '^hippoTree',
        link: function (scope, element, attrs, controller) {
          controller.renderTreeTemplate(scope, function (dom) {
            element.replaceWith(dom);
          });
        }
      };
    })

    .directive('hippoNodeTemplateUrl', [
      '$compile',
      '$http',
      '$templateCache',
      function ($compile, $http, $templateCache) {
        return {
          restrict: 'A',
          link: function (scope, element, attr) {
            var templateUrl = attr.hippoNodeTemplateUrl;
            var innerTemplate = $templateCache.get(templateUrl);

            if (typeof innerTemplate === 'undefined') {
              $http.get(templateUrl).then(function (response) {
                innerTemplate = response.data;
                $templateCache.put(templateUrl, innerTemplate);

                $compile(innerTemplate)(scope, function (clone) {
                  element.append(clone);
                });
              });
            } else {
              $compile(innerTemplate)(scope, function (clone) {
                element.append(clone);
              });
            }
          }
        };
      }
    ]);
})();

/*
 * Copyright 2014-2015 Hippo B.V. (http://www.onehippo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

(function () {
  "use strict";

  angular.module('hippo.theme')
  /**
   * @ngdoc directive
   * @name hippo.theme.directive:typeaheadFocus
   *
   * @description
   * Show the typeahead on focus
   * Credits to yohairosen: http://stackoverflow.com/questions/24764802/angular-js-automatically-focus-input-and-show-typeahead-dropdown-ui-bootstra#answer-27331340
   */
    .directive('typeaheadFocus', function () {
      return {
        require: 'ngModel',
        link: function (scope, element, attr, ngModel) {
          element.bind('focus', function () {

            var viewValue = ngModel.$viewValue;

            //restore to null value so that the typeahead can detect a change
            if (ngModel.$viewValue == ' ') {
              ngModel.$setViewValue(null);
            }

            //force trigger the popup
            ngModel.$setViewValue(' ');

            //set the actual value in case there was already a value in the input
            ngModel.$setViewValue(viewValue || ' ');
          });

          //compare function that treats the empty space as a match
          scope.emptyOrMatch = function (actual, expected) {
            if (expected == ' ') {
              return true;
            }
            return actual.indexOf(expected) > -1;
          };
        }
      };
    }
  );
})();

//# sourceMappingURL=main.js.map