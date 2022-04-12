"use strict";(self.webpackChunkkotestdocs=self.webpackChunkkotestdocs||[]).push([[2468],{3905:function(e,t,n){n.d(t,{Zo:function(){return p},kt:function(){return m}});var r=n(7294);function a(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function o(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function i(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?o(Object(n),!0).forEach((function(t){a(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):o(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function l(e,t){if(null==e)return{};var n,r,a=function(e,t){if(null==e)return{};var n,r,a={},o=Object.keys(e);for(r=0;r<o.length;r++)n=o[r],t.indexOf(n)>=0||(a[n]=e[n]);return a}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(r=0;r<o.length;r++)n=o[r],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(a[n]=e[n])}return a}var s=r.createContext({}),c=function(e){var t=r.useContext(s),n=t;return e&&(n="function"==typeof e?e(t):i(i({},t),e)),n},p=function(e){var t=c(e.components);return r.createElement(s.Provider,{value:t},e.children)},u={inlineCode:"code",wrapper:function(e){var t=e.children;return r.createElement(r.Fragment,{},t)}},d=r.forwardRef((function(e,t){var n=e.components,a=e.mdxType,o=e.originalType,s=e.parentName,p=l(e,["components","mdxType","originalType","parentName"]),d=c(n),m=a,f=d["".concat(s,".").concat(m)]||d[m]||u[m]||o;return n?r.createElement(f,i(i({ref:t},p),{},{components:n})):r.createElement(f,i({ref:t},p))}));function m(e,t){var n=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var o=n.length,i=new Array(o);i[0]=d;var l={};for(var s in t)hasOwnProperty.call(t,s)&&(l[s]=t[s]);l.originalType=e,l.mdxType="string"==typeof e?e:a,i[1]=l;for(var c=2;c<o;c++)i[c]=n[c];return r.createElement.apply(null,i)}return r.createElement.apply(null,n)}d.displayName="MDXCreateElement"},3139:function(e,t,n){n.r(t),n.d(t,{frontMatter:function(){return l},contentTitle:function(){return s},metadata:function(){return c},toc:function(){return p},default:function(){return d}});var r=n(7462),a=n(3366),o=(n(7294),n(3905)),i=["components"],l={},s=void 0,c={unversionedId:"framework/datatesting/data_driven_testing_4.2.0",id:"framework/datatesting/data_driven_testing_4.2.0",isDocsHomePage:!1,title:"data_driven_testing_4.2.0",description:"To test your code with different parameter combinations, you can use a table of values as input for your test",source:"@site/docs/framework/datatesting/data_driven_testing_4.2.0.md",sourceDirName:"framework/datatesting",slug:"/framework/datatesting/data_driven_testing_4.2.0",permalink:"/docs/framework/datatesting/data_driven_testing_4.2.0",editUrl:"https://github.com/kotest/kotest/blob/master/documentation/docs/framework/datatesting/data_driven_testing_4.2.0.md",tags:[],version:"current",frontMatter:{}},p=[],u={toc:p};function d(e){var t=e.components,n=(0,a.Z)(e,i);return(0,o.kt)("wrapper",(0,r.Z)({},u,n,{components:t,mdxType:"MDXLayout"}),(0,o.kt)("p",null,"To test your code with different parameter combinations, you can use a table of values as input for your test\ncases. This is called ",(0,o.kt)("em",{parentName:"p"},"data driven testing")," also known as ",(0,o.kt)("em",{parentName:"p"},"table driven testing"),"."),(0,o.kt)("p",null,"Invoke the ",(0,o.kt)("inlineCode",{parentName:"p"},"forAll")," or ",(0,o.kt)("inlineCode",{parentName:"p"},"forNone")," function, passing in one or more ",(0,o.kt)("inlineCode",{parentName:"p"},"row")," objects, where each row object contains\nthe values to be used be a single invocation of the test. After the ",(0,o.kt)("inlineCode",{parentName:"p"},"forAll")," or ",(0,o.kt)("inlineCode",{parentName:"p"},"forNone")," function, setup your\nactual test function to accept the values of each row as parameters."),(0,o.kt)("p",null,"The row object accepts any set of types, and the type checker will ensure your types are consistent with the parameter\ntypes in the test function."),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-kotlin"},'"square roots" {\n  forAll(\n      row(2, 4),\n      row(3, 9),\n      row(4, 16),\n      row(5, 25)\n  ) { root, square ->\n    root * root shouldBe square\n  }\n}\n')),(0,o.kt)("p",null,"In the above example, the ",(0,o.kt)("inlineCode",{parentName:"p"},"root")," and ",(0,o.kt)("inlineCode",{parentName:"p"},"square")," parameters are automatically inferred to be integers."),(0,o.kt)("p",null,"If there is an error for any particular input row, then the test will fail and KotlinTest will automatically\nmatch up each input to the corresponding parameter names. For example, if we change the previous example to include the row ",(0,o.kt)("inlineCode",{parentName:"p"},"row(5,55)"),"\nthen the test will be marked as a failure with the following error message."),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre"},"Test failed for (root, 5), (square, 55) with error expected: 55 but was: 25\n")),(0,o.kt)("p",null,"Table testing can be used within any spec. Here is an example using ",(0,o.kt)("inlineCode",{parentName:"p"},"StringSpec"),"."),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-kotlin"},'class StringSpecExample : StringSpec({\n  "string concat" {\n    forAll(\n      row("a", "b", "c", "abc"),\n      row("hel", "lo wo", "rld", "hello world"),\n      row("", "z", "", "z")\n    ) { a, b, c, d ->\n      a + b + c shouldBe d\n    }\n  }\n})\n')),(0,o.kt)("p",null,"It may be desirable to have each row of data parameters as an individual test. To generating such individual tests follow a similar pattern for each spec style. An example in the ",(0,o.kt)("inlineCode",{parentName:"p"},"FreeSpec")," is below."),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-kotlin"},'class IntegerMathSpec : FreeSpec({\n    "Addition" - {\n        listOf(\n            row("1 + 0", 1) { 1 + 0 },\n            row("1 + 1", 2) { 1 + 1 }\n        ).map { (description: String, expected: Int, math: () -> Int) ->\n            description {\n                math() shouldBe expected\n            }\n        }\n    }\n    // ...\n    "Complex Math" - {\n        listOf(\n            row("8/2(2+2)", 16) { 8 / 2 * (2 + 2) },\n            row("5/5 + 1*1 + 3-2", 3) { 5 / 5 + 1 * 1 + 3 - 2 }\n        ).map { (description: String, expected: Int, math: () -> Int) ->\n            description {\n                math() shouldBe expected\n            }\n        }\n    }\n})\n')),(0,o.kt)("p",null,"Produces 4 tests and 2 parent descriptions:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-txt"},"IntegerMathSpec\n  \u2713 Addition\n    \u2713 1 + 0\n    \u2713 1 + 1\n  \u2713 Complex Math\n    \u2713 8/2(2+2)\n    \u2713 5/5 + 1*1 + 3-2\n")))}d.isMDXComponent=!0}}]);