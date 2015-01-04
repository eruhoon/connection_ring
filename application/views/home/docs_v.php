<div class="container">

	<div class="page-header">
		<h2><i class="fa fa-file-text-o"></i> Documentation </h2>
		<p> 위젯 제작에 대한 Element 설명이 담긴 페이지입니다. </p>
	</div>
	<br/>

	<!-- COMPONENT -->
	<div>
		<h3><i class="fa fa-tag"></i> COMPONENT</h3>
		<p>
			<code>&lt;component></code>는 위젯 작성을 위해 전체 컴포넌트를 감싸는 최상위 엘리먼트입니다. <br/>
			전체적인 컴포넌트의 크기를 판단하며, 자식 엘리먼트를 추가함으로써 위젯의 레이아웃을 구성할 수 있도록 돕는 역할을 수행합니다. <br/>
			가장 최상단에 추가되며, 작성하는 레이아웃 하나당 하나의 엘리먼트를 가집니다.<br/>
			자식 엘리먼트로 <code>&lt;constants></code>, <code>&lt;memo></code> 등을 사용할 수 있습니다.
		</p>
		<div class="well">
			<p>
				<i class="fa fa-tag"></i> 엘리먼트 관계 <br/>
			</p>
			<dl class="dl-horizontal">
				<dt>부모 엘리먼트</dt>
				<dd>
					<p>없음</p>
				</dd>
				<dt>자식 엘리먼트</dt>
				<dd>
					<p>
						<code>&lt;memo></code>,
						<code>&lt;calender></code>,
						<code>&lt;constants></code>,
						<code>&lt;webparser></code>,
						<code>&lt;layer></code>,
						<code>&lt;label></code>
					</p>
				</dd>
			</dl>
			<hr>
			<p>
				<i class="fa fa-tasks"></i> 속성 <br/>
			</p>

			<dl class="dl-horizontal">
				<dt><code>name</code></dt>
				<dd><p>위젯의 이름을 지정합니다.</p></dd>
				<dt><code>width</code></dt>
				<dd><p>위젯 영역의 가로길이를 지정합니다.</p></dd>
				<dt><code>height</code></dt>
				<dd><p>위젯 영역의 세로길이를 지정합니다.</p></dd>
			</dl>
		</div>
	</div>

	

	<!-- BACKGROUND -->
	<br><br><br><br>
	<div>
		<h3><i class="fa fa-tag"></i> BACKGROUND</h3>
		<p>
			<code>&lt;background></code>는 위젯의 배경을 지정해주는 엘리먼트입니다.<br/>
			엘리먼트를 포함하는 부모 엘리먼트의 배경을 이미지 또는 색깔로 표현할 수 있습니다. <br/>
			<code>image</code>, <code>color</code> 속성을 가지고 있으며, 둘 중 하나를 택하여 배경을 표현합니다. <br/>
			두가지의 속성을 모두 가지고 있다면, <code>image</code> 속성을 우선적으로 판단하여 처리합니다.<br/>
			한 엘리먼트에 중복된 두개의 <code>&lt;background></code> 엘리먼트를 가지고 있으면, 먼저 입력된 엘리먼트를 적용합니다.<br/>
		</p>
		<div class="well">
			<p>
				<i class="fa fa-tag"></i> 엘리먼트 관계 <br/>
			</p>
			<dl class="dl-horizontal">
				<dt>부모 엘리먼트</dt>
				<dd>
					<p><code>&lt;layer></code></p>
				</dd>
				<dt>자식 엘리먼트</dt>
				<dd>
					<p>없음</p>
				</dd>
			</dl>
			<hr>
			<p>
				<i class="fa fa-tasks"></i> 속성 <br/>
			</p>

			<dl class="dl-horizontal">
				<dt><code>image</code></dt>
				<dd><p>부모 엘리먼트의 배경이미지를 지정합니다. <code>url</code>로 표현합니다.</p></dd>
				<dt><code>color</code></dt>
				<dd><p>부모 엘리먼트의 배경색을 지정합니다. <code>#FFFFFF</code>과 같은 방법으로 표현합니다.</p></dd>
			</dl>
		</div>
	</div>



	<!-- POSITION -->
	<br><br><br><br>
	<div>
		<h3><i class="fa fa-tag"></i> POSITION</h3>
		<p>
			<code>&lt;position></code>는 Layer나 memo등의 상위 엘리먼트의 위치를 지정해주는 엘리먼트입니다.<br/>
			<code>&lt;layer></code>와 <code>&lt;label></code> 등의 레이아웃을 구성하는 엘리먼트들이 부모 엘리먼트가 될 수 있습니다. <br/>
			각 엘리먼트 객체들은 좌측 위를 앵커로 가지고 있고 각 x, y의 값은 html의 left, top과 대응합니다.<br/>
			한 엘리먼트에 중복된 두개의 <code>&lt;position></code> 엘리먼트를 가지고 있으면, 먼저 입력된 엘리먼트를 적용합니다.<br/>
		</p>
		<div class="well">
			<p>
				<i class="fa fa-tag"></i> 엘리먼트 관계 <br/>
			</p>
			<dl class="dl-horizontal">
				<dt>부모 엘리먼트</dt>
				<dd>
					<p><code>&lt;component></code>, <code>&lt;webparser></code>, <code>&lt;constants></code>, <code>&lt;var></code>을 제외한 나머지 모두</p>
				</dd>
				<dt>자식 엘리먼트</dt>
				<dd>
					<p>없음</p>
				</dd>
			</dl>
			<hr>
			<p>
				<i class="fa fa-tasks"></i> 속성 <br/>
			</p>

			<dl class="dl-horizontal">
				<dt><code>x</code></dt>
				<dd><p>부모 엘리먼트의 left값을 지정합니다.</p></dd>
				<dt><code>y</code></dt>
				<dd><p>부모 엘리먼트의 top값을 지정합니다.</p></dd>
				<dt><code>width</code></dt>
				<dd><p>부모 엘리먼트의 너비를 지정합니다.</p></dd>
				<dt><code>height</code></dt>
				<dd><p>부모 엘리먼트의 높이를 지정합니다.</p></dd>
			</dl>
		</div>
	</div>




	<!-- LAYER -->
	<br><br><br><br>
	<div>
		<h3><i class="fa fa-tag"></i> LAYER</h3>
		<p>
			<code>&lt;layer></code>는 레이아웃의 그래픽적 요소를 표현하기 위한 엘리먼트입니다.<br/>
			<code>&lt;background></code> 엘리먼트를 이용하여 배경색 혹은 이미지를 지정할 수 있습니다. <br/>
			한 엘리먼트에 여러 <code>&lt;layer></code>를 종속하는 것이 가능하며 겹치는 부분에 대하여 가장 최근에 파싱된 레이어가 맨 위에 표현됩니다.<br/>
		</p>
		<div class="well">
			<p>
				<i class="fa fa-tag"></i> 엘리먼트 관계 <br/>
			</p>
			<dl class="dl-horizontal">
				<dt>부모 엘리먼트</dt>
				<dd>
					<p><code>&lt;component></code></p>
				</dd>
				<dt>자식 엘리먼트</dt>
				<dd>
					<p>
						<code>&lt;background></code>,
						<code>&lt;position></code>
					</p>
				</dd>
			</dl>
		</div>
	</div>



	<!-- MEMO -->
	<br><br><br><br>
	<div>
		<h3><i class="fa fa-tag"></i> MEMO</h3>
		<p>
			<code>&lt;memo></code>는 레이아웃에 필요한 메모 UI를 표현하기 위한 엘리먼트입니다.<br/>
			한 엘리먼트에 중복된 두개의 <code>&lt;memo></code> 엘리먼트를 가지고 있으면, 먼저 입력된 엘리먼트를 적용합니다.<br/>
		</p>
		<div class="well">
			<p>
				<i class="fa fa-tag"></i> 엘리먼트 관계 <br/>
			</p>
			<dl class="dl-horizontal">
				<dt>부모 엘리먼트</dt>
				<dd>
					<p><code>&lt;component></code></p>
				</dd>
				<dt>자식 엘리먼트</dt>
				<dd>
					<p>
						<code>&lt;textarea></code>,
						<code>&lt;agenda></code>,
						<code>&lt;button_memo></code>,
						<code>&lt;position></code>
					</p>
				</dd>
			</dl>
		</div>
	</div>



	<!-- TEXTAREA -->
	<br><br><br><br>
	<div>
		<h3><i class="fa fa-tag"></i> TEXTAREA</h3>
		<p>
			<code>&lt;textarea></code>는 컴포넌트가 갖고있는 메모의 내용을 입력, 출력이  가능한 영역을 설정하는 엘리먼트입니다.<br/>
			한 엘리먼트에 중복된 두개의 <code>&lt;textarea></code> 엘리먼트를 가지고 있으면, 먼저 입력된 엘리먼트를 적용합니다.<br/>
		</p>
		<div class="well">
			<p>
				<i class="fa fa-tag"></i> 엘리먼트 관계 <br/>
			</p>
			<dl class="dl-horizontal">
				<dt>부모 엘리먼트</dt>
				<dd>
					<p><code>&lt;memo></code></p>
				</dd>
				<dt>자식 엘리먼트</dt>
				<dd>
					<p><code>&lt;position></code></p>
				</dd>
			</dl>
			<hr>
			<p><i class="fa fa-tasks"></i> 속성 <br/></p>

			<dl class="dl-horizontal">
				<dt><code>fontsize</code></dt>
				<dd><p>글씨의 크기를 지정합니다.</p></dd>
			</dl>
		</div>
	</div>






	<!-- AGENDA -->
	<br><br><br><br>
	<div>
		<h3><i class="fa fa-tag"></i> AGENDA</h3>
		<p>
			<code>&lt;agenda></code>는 컴포넌트가 갖고있는 메모의 일정을 출력하는 영역을 설정하는 엘리먼트입니다.<br/>
			한 엘리먼트에 중복된 두개의 <code>&lt;agenda></code> 엘리먼트를 가지고 있으면, 먼저 입력된 엘리먼트를 적용합니다.<br/>
		</p>
		<div class="well">
			<p>
				<i class="fa fa-tag"></i> 엘리먼트 관계 <br/>
			</p>
			<dl class="dl-horizontal">
				<dt>부모 엘리먼트</dt>
				<dd>
					<p><code>&lt;memo></code></p>
				</dd>
				<dt>자식 엘리먼트</dt>
				<dd>
					<p><code>&lt;position></code></p>
				</dd>
			</dl>
			<hr>
			<p><i class="fa fa-tasks"></i> 속성 <br/></p>

			<dl class="dl-horizontal">
				<dt><code>fontsize</code></dt>
				<dd><p>글씨의 크기를 지정합니다.</p></dd>
			</dl>
		</div>
	</div>






	<!-- BUTTON_MEMO -->
	<br><br><br><br>
	<div>
		<h3><i class="fa fa-tag"></i> BUTTON @ MEMO</h3>
		<p>
			<code>&lt;button_memo></code>는 컴포넌트가 갖고있는 메모를 조작하는 기능을 가진 버튼을 생성하는 엘리먼트입니다.<br/>
		</p>
		<div class="well">
			<p>
				<i class="fa fa-tag"></i> 엘리먼트 관계 <br/>
			</p>
			<dl class="dl-horizontal">
				<dt>부모 엘리먼트</dt>
				<dd>
					<p><code>&lt;memo></code></p>
				</dd>
				<dt>자식 엘리먼트</dt>
				<dd>
					<p><code>&lt;position></code></p>
				</dd>
			</dl>
			<hr>
			<p><i class="fa fa-tasks"></i> 속성 <br/></p>

			<dl class="dl-horizontal">
				<dt><code>value</code></dt>
				<dd><p>버튼에 표시될 Text값을 지정합니다.</p></dd>
				<dt><code>command</code></dt>
				<dd><p>버튼에 부여할 기능을 지정합니다. 각각 <code>delete</code>, <code>hold</code>, <code>url</code>, <code>link</code>의 기능을 수행할 수 있습니다. </p></dd>
				<dt><code>src_normal</code></dt>
				<dd><p>버튼에 추가될 이미지를 지정합니다.</p></dd>
				<dt><code>src_onmouseover</code></dt>
				<dd><p>마우스가 버튼에 위치했을 때 이미지를 지정합니다.</p></dd>
				<dt><code>src_onmousedown</code></dt>
				<dd><p>마우스로 버튼을 누르고 있을 때 이미지를 지정합니다.</p></dd>
			</dl>
		</div>
	</div>









	<!-- CALENDAR -->
	<br><br><br><br>
	<div>
		<h3><i class="fa fa-tag"></i> CALENDAR</h3>
		<p>
			<code>&lt;calendar></code>는 레이아웃에 필요한 캘린더 UI를 표현하기 위한 엘리먼트입니다.<br/>
			한 엘리먼트에 중복된 두개의 <code>&lt;calendar></code> 엘리먼트를 가지고 있으면, 먼저 입력된 엘리먼트를 적용합니다.<br/>
		</p>
		<div class="well">
			<p>
				<i class="fa fa-tag"></i> 엘리먼트 관계 <br/>
			</p>
			<dl class="dl-horizontal">
				<dt>부모 엘리먼트</dt>
				<dd>
					<p><code>&lt;component></code></p>
				</dd>
				<dt>자식 엘리먼트</dt>
				<dd>
					<p><code>&lt;position></code>,<code>&lt;year></code>,<code>&lt;month></code>,<code>&lt;date></code>,<code>&lt;button_cal></code></p>
				</dd>
			</dl>
		</div>
	</div>







	<!-- YEAR -->
	<br><br><br><br>
	<div>
		<h3><i class="fa fa-tag"></i> YEAR</h3>
		<p>
			<code>&lt;year></code>는 달력의 년도를 표시하기 위한 엘리먼트입니다.<br/>
			한 엘리먼트에 중복된 두개의 <code>&lt;year></code> 엘리먼트를 가지고 있으면, 먼저 입력된 엘리먼트를 적용합니다.<br/>
		</p>
		<div class="well">
			<p>
				<i class="fa fa-tag"></i> 엘리먼트 관계 <br/>
			</p>
			<dl class="dl-horizontal">
				<dt>부모 엘리먼트</dt>
				<dd>
					<p><code>&lt;calendar></code></p>
				</dd>
				<dt>자식 엘리먼트</dt>
				<dd>
					<p><code>&lt;position></code></p>
				</dd>
			</dl>
			<hr>
			<p><i class="fa fa-tasks"></i> 속성 <br/></p>

			<dl class="dl-horizontal">
				<dt><code>src</code></dt>
				<dd><p>연도를 표시하는데 사용될 이미지의 경로를 지정합니다.</p></dd>
				<dt><code>offset</code></dt>
				<dd><p>지정한 이미지에서 한글자가 차지하는 가로 Pixel을 지정합니다.</p></dd>
			</dl>
		</div>
	</div>







	<!-- MONTH -->
	<br><br><br><br>
	<div>
		<h3><i class="fa fa-tag"></i> MONTH</h3>
		<p>
			<code>&lt;month></code>는 달력의 월수를 표시하기 위한 엘리먼트입니다.<br/>
			한 엘리먼트에 중복된 두개의 <code>&lt;month></code> 엘리먼트를 가지고 있으면, 먼저 입력된 엘리먼트를 적용합니다.<br/>
		</p>
		<div class="well">
			<p>
				<i class="fa fa-tag"></i> 엘리먼트 관계 <br/>
			</p>
			<dl class="dl-horizontal">
				<dt>부모 엘리먼트</dt>
				<dd>
					<p><code>&lt;calendar></code></p>
				</dd>
				<dt>자식 엘리먼트</dt>
				<dd>
					<p><code>&lt;position></code></p>
				</dd>
			</dl>
			<hr>
			<p><i class="fa fa-tasks"></i> 속성 <br/></p>

			<dl class="dl-horizontal">
				<dt><code>src</code></dt>
				<dd><p>월수를 표시하는데 사용될 이미지의 경로를 지정합니다.</p></dd>
				<dt><code>offset</code></dt>
				<dd><p>지정한 이미지에서 한글자가 차지하는 가로 Pixel을 지정합니다.</p></dd>
			</dl>
		</div>
	</div>








	<!-- DAY -->
	<br><br><br><br>
	<div>
		<h3><i class="fa fa-tag"></i> DATE</h3>
		<p>
			<code>&lt;date></code>는 달력의 일수를 표시하기 위한 엘리먼트입니다.<br/>
			한 엘리먼트에 중복된 두개의 <code>&lt;date></code> 엘리먼트를 가지고 있으면, 먼저 입력된 엘리먼트를 적용합니다.<br/>
		</p>
		<div class="well">
			<p>
				<i class="fa fa-tag"></i> 엘리먼트 관계 <br/>
			</p>
			<dl class="dl-horizontal">
				<dt>부모 엘리먼트</dt>
				<dd>
					<p><code>&lt;calendar></code></p>
				</dd>
				<dt>자식 엘리먼트</dt>
				<dd>
					<p><code>&lt;position></code></p>
				</dd>
			</dl>
			<hr>
			<p><i class="fa fa-tasks"></i> 속성 <br/></p>

			<dl class="dl-horizontal">
				<dt><code>column</code></dt>
				<dd><p>가로 열 개수를 지정합니다. Default 값은 0이며, 0의 경우 일반적인 달력과 같은 형태로 출력됩니다. 그러나 0이 아닌 다른 양수 값일 경우 해당 값에 해당하는 만큼의 열을 가지고 출력되며 요일과 무관하게 출력됩니다. </p></dd>
				<dt><code>src</code></dt>
				<dd><p>일수를 표시하는데 사용될 이미지의 경로를 지정합니다.</p></dd>
				<dt><code>sundaysrc</code></dt>
				<dd><p>일요일에 해당하는 날짜를 표시하는데 사용될 이미지의 경로를 지정합니다.</p></dd>
				<dt><code>saturdaysrc</code></dt>
				<dd><p>토요일에 해당하는 날짜를 표시하는데 사용될 이미지의 경로를 지정합니다.</p></dd>
				<dt><code>offset</code></dt>
				<dd><p>지정한 이미지에서 한글자가 차지하는 가로 Pixel을 지정합니다.</p></dd>
			</dl>
		</div>
	</div>










	<!-- BUTTON_CALENDAR -->
	<br><br><br><br>
	<div>
		<h3><i class="fa fa-tag"></i> BUTTON @ CALENDAR</h3>
		<p>
			<code>&lt;button_cal></code>는 컴포넌트가 갖고있는 달력을 조작하는 기능을 가진 버튼을 생성하는 엘리먼트입니다.<br/>
		</p>
		<div class="well">
			<p>
				<i class="fa fa-tag"></i> 엘리먼트 관계 <br/>
			</p>
			<dl class="dl-horizontal">
				<dt>부모 엘리먼트</dt>
				<dd>
					<p><code>&lt;memo></code></p>
				</dd>
				<dt>자식 엘리먼트</dt>
				<dd>
					<p><code>&lt;position></code></p>
				</dd>
			</dl>
			<hr>
			<p><i class="fa fa-tasks"></i> 속성 <br/></p>

			<dl class="dl-horizontal">
				<dt><code>value</code></dt>
				<dd><p>버튼에 표시될 Text값을 지정합니다.</p></dd>
				<dt><code>command</code></dt>
				<dd><p>버튼에 부여할 기능을 지정합니다. 각각 <code>prevmonth</code>, <code>nextmonth</code>의 기능을 수행할 수 있습니다. </p></dd>
				<dt><code>src_normal</code></dt>
				<dd><p>버튼에 추가될 이미지를 지정합니다.</p></dd>
				<dt><code>src_onmouseover</code></dt>
				<dd><p>마우스가 버튼에 위치했을 때 이미지를 지정합니다.</p></dd>
				<dt><code>src_onmousedown</code></dt>
				<dd><p>마우스로 버튼을 누르고 있을 때 이미지를 지정합니다.</p></dd>
			</dl>
		</div>
	</div>












	<!-- CONSTANTS -->
	<br><br><br><br>
	<div>
		<h3><i class="fa fa-tag"></i> COSNTANTS</h3>
		<p>
			<code>&lt;constants></code>는 위젯에서 미리 사용할 상수들을 지정해주는 엘리먼트입니다.<br/>
			이 엘리먼트의 추가로 인하여 생기는 레이아웃변화는 없습니다. <br/>
			<code>&lt;var></code>의 <code>key</code>, <code>value</code>를 이용하여 상수값을 등록하고 상수가 필요한 부분에 지정된 포맷을 사용하여 적용할 수 있습니다. <br/>
			반복된 값을 가진 속성을 일괄적으로 처리할때 효과적입니다. <br/>
			한 엘리먼트에 중복된 두개의 <code>&lt;constants></code> 엘리먼트를 가지고 있으면, 먼저 입력된 엘리먼트를 적용합니다.<br/>
		</p>
		<div class="well">
			<p><i class="fa fa-tag"></i> 엘리먼트 관계 <br/></p>
			<dl class="dl-horizontal">
				<dt>부모 엘리먼트</dt>
				<dd>
					<p><code>&lt;component></code></p>
				</dd>
				<dt>자식 엘리먼트</dt>
				<dd>
					<p><code>&lt;var></code></p>
				</dd>
			</dl>
			<hr>
			<p><i class="fa fa-tasks"></i> 예시 <br/></p>
			<dl class="dl-horizaontal">
				<dt> 예시코드</dt>
				<dd>
<pre id='constant_example' style='height:90px'>
&lt;constants>
	&lt;var key="example" value="300" />
&lt;/constants>
&lt;layer>
	&lt;position x="300" y="300" width="<code>##example##</code>" height />
&lt;/layer></pre>
				</dd>
				<dt> 결과 </dt>
				<dd>
<pre id="constant_example2" style="height:50px">
&lt;layer>
	&lt;position x="300" y="300" width="<code>300</code>" height />
&lt;/layer></pre>
				</dd>
			</dl>
		</div>
	</div>




	<!-- VAR -->
	<br><br><br><br>
	<div>
		<h3><i class="fa fa-tag"></i> VAR</h3>
		<p>
			<code>&lt;var></code>는 <code>&lt;constants></code>에 종속된 엘리먼트이며 상수를 지정해주는 엘리먼트입니다. <br/>
		</p>
		<div class="well">
			<p><i class="fa fa-tag"></i> 엘리먼트 관계 <br/></p>
			<dl class="dl-horizontal">
				<dt>부모 엘리먼트</dt>
				<dd>
					<p><code>&lt;constants></code></p>
				</dd>
				<dt>자식 엘리먼트</dt>
				<dd>
					<p>없음</p>
				</dd>
			</dl>
			<hr>
			<p><i class="fa fa-tasks"></i> 속성 <br/></p>
			<dl class="dl-horizontal">
				<dt><code>key</code></dt>
				<dd><p>상수의 이름을 지정합니다. 이후 사용시 <code>##key##</code>를 이용하여 적용합니다.</p></dd>
				<dt><code>value</code></dt>
				<dd><p>상수의 값을 지정합니다.</p></dd>
			</dl>
		</div>
	</div>




	<!-- WEBPARSER -->
	<br><br><br><br>
	<div>
		<h3><i class="fa fa-tag"></i> WEBPARSER</h3>
		<p>
			<code>&lt;webparser></code>는 웹에서 파싱된 데이터를 상수화하는데 도움을 주는 엘리먼트입니다.<br/>
			<code>url</code>과 <code>regex</code> 속성을 가지고 있으며, 이를 통해 웹에서 필요한 내용을 파싱하는데 이용됩니다. <br/>
			한 엘리먼트에 중복된 두개의 <code>&lt;webparser></code> 엘리먼트를 가지고 있으면, 먼저 입력된 엘리먼트를 적용합니다.<br/>
		</p>
		<div class="well">
			<p>
				<i class="fa fa-tag"></i> 엘리먼트 관계 <br/>
			</p>
			<dl class="dl-horizontal">
				<dt>부모 엘리먼트</dt>
				<dd>
					<p><code>&lt;component></code></p>
				</dd>
				<dt>자식 엘리먼트</dt>
				<dd>
					<p>없음</p>
				</dd>
			</dl>
			<hr>
			<p>
				<i class="fa fa-tasks"></i> 속성 <br/>
			</p>

			<dl class="dl-horizontal">
				<dt><code>url</code></dt>
				<dd><p>데이터를 파싱할 페이지의 Url을 지정합니다.</p></dd>
				<dt><code>regex</code></dt>
				<dd><p>데이터 파싱을 위한 정규표현식을 지정합니다.<br>
				본 서비스는 PCRE를 기준으로 데이터 처리가 이루어지고 있습니다.<br>
				매칭된 결과는 <code>##WP[숫자]##</code>의 형식으로 적용할 수 있습니다.
				</p></dd>
			</dl>
			<hr>
			<p><i class="fa fa-tasks"></i> 예시 <br/></p>
			<dl class="dl-horizaontal">
				<dt> 예시코드</dt>
				<dd>
매칭된 결과물의 3번째 항목이 "abcd"라 가정하면
<pre id='webparser_example' style='height:80px'>
&lt;webparser url="http://host/test/test.xml" regex="(?=&lt;var name=id).*&lt;string>(.*)&lt;\/string>">
&lt;/webparser>
&lt;layer>
	&lt;background image="http://host/images/<code>##WP3##</code>/" />
&lt;/layer></pre>
				</dd>
				<dt> 결과 </dt>
				<dd>
<pre id="webparser_example2" style="height:50px">
&lt;layer>
	&lt;background image="http://host/images/<code>abcd</code>/" />
&lt;/layer></pre>
				</dd>
			</dl>
		</div>
	</div>



</div>

<!-- SPINNER -->
<div id="spinner" class="spinner" style="padding-top: 300px">
	로딩 중입니다.<br>
	<br>
	<i class="fa fa-5x fa-spinner fa-spin"></i>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/ace/1.1.3/ace.js" type="text/javascript" charset="utf-8"></script>



<script>
$(window).load(function(){
	$('#spinner').addClass('hide');
})

var editorList = ['constant_example', 'constant_example2', 'webparser_example', 'webparser_example2'];
for(var idx=0; idx<editorList.length; idx++){
	var editor = ace.edit(editorList[idx]);
	editor.setTheme("ace/theme/twilight");
	editor.getSession().setMode("ace/mode/xml");
	editor.getSession().setUseWrapMode(true);
	editor.getSession().on("change", function(e){
		edited = true;
	});	
}
</script>