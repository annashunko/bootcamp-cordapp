package java_bootcamp;

import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

import static net.corda.core.contracts.ContractsDSL.requireThat;

/* Our contract, governing how our state will evolve over time.
 * See src/main/java/examples/ArtContract.java for an example. */
public class TokenContract implements Contract {

    public static String ID = "java_bootcamp.TokenContract";

    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {
        asset(tx.getCommands().size() == 1, "One command allowed");
        asset(tx.getInputs().size() == 0, "There should be no inputs");
        asset(tx.getOutputs().size() == 1, "There should be one output");

        ContractState output = tx.getOutput(0);
        asset(output instanceof TokenState, "Output should be TokenState");
        asset(((TokenState) output).getAmount() > 0, "Amount should be more than 0");
        asset(tx.getCommand(0).getValue() instanceof Commands.Issue, "Command should be Issue");
        asset(tx.getCommand(0).getSigners().contains(((TokenState) output).getIssuer().getOwningKey()), "Issuer should be signer");
    }

    private void asset(boolean expresion, String message) {
        if (!expresion) {
            throw new IllegalArgumentException(message);
        }
    }

    public interface Commands extends CommandData {
        class Issue implements Commands {
        }
    }

}