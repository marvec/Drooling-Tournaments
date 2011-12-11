for (int i = 25; i <= 1000; i += 25) {
for (int j = 1; j <= 64; j *= 2) {

def solver = """
  <solverBenchmark>
    <name>Accept $i soft, minimalAcceptSolution $j</name>
    <solver>
    <localSearch>
      <termination>
          <terminationCompositionStyle>OR</terminationCompositionStyle>
          <maximumUnimprovedStepCount>100</maximumUnimprovedStepCount>
          <scoreAttained>0hard/0soft</scoreAttained>
      </termination> 
      <acceptor>
         <simulatedAnnealingStartingTemperature>1hard/0soft</simulatedAnnealingStartingTemperature>
      </acceptor>
      <forager>
         <minimalAcceptedSelection>4</minimalAcceptedSelection>
      </forager>    
      <selector>
         <selector>
             <moveFactoryClass>org.drools.planner.examples.tournaments.move.factory.FillSlotMoveFactory</moveFactoryClass>
         </selector>
           <selector>
             <moveFactoryClass>org.drools.planner.examples.tournaments.move.factory.SwitchSlotMoveFactory</moveFactoryClass>
           </selector>
      </selector>
    </localSearch>
    <localSearch>
    <termination>
        <terminationCompositionStyle>OR</terminationCompositionStyle>
        <maximumUnimprovedStepCount>200</maximumUnimprovedStepCount>
        <scoreAttained>0hard/0soft</scoreAttained>
    </termination> 
    <acceptor>
       <simulatedAnnealingStartingTemperature>0hard/${i}soft</simulatedAnnealingStartingTemperature>
    </acceptor>
    <forager>
       <minimalAcceptedSelection>$j</minimalAcceptedSelection>
    </forager>    
    <selector>
           <selector>
           <moveFactoryClass>org.drools.planner.examples.tournaments.move.factory.SwitchSlotMoveFactory</moveFactoryClass>
         </selector>
    </selector>
    </localSearch>
    </solver>
  </solverBenchmark>
"""

println solver

}
}