# Senior Accounting Officer Frontend Journey Tests

>  **Current chrome issue**:
> [21/11/25] There is a current issue when running tests using later versions of chrome as per https://hmrcdigital.slack.com/archives/C0J8BH46N/p1760607965741119.
As a workaround we must use version 128. In order to do so we must also add the following properties to the CLI
>>` -Dbrowser.usePreviousVersion=true `
>
>For the foreseeable future (this applies to all subsequent commands associated with chrome)
e.g. Run the following command to trigger the test via the CLI.
>>sbt clean -Dbrowser="chrome" -Denvironment="local"  -Dbrowser.usePreviousVersion=true test
>
>The scripts are also updated, and once this is fixed we'll need to remove them

## Services

**Run the following command to start services locally:**

```bash
sm2 --start SAO_ALL
```

## Tests

**Run tests as follows:**

* Argument `<browser>` must be `chrome`, `edge`, or `firefox`.
* Argument `<environment>` must be `local`, `dev`, `qa` or `staging`.

```bash
sbt clean -Dbrowser="chrome" -Denvironment="local" test
```

**Run all tests:**

The `run_tests.sh` script defaults to using `chrome` in the `local` environment.

```bash
 ./run_tests.sh
```

**To run any test individually, add "@solo" tag to the respective file and use:**

```bash
 ./run_solo_tests.sh
```

**Executing a local ZAP test:**

First [run the DAST tool locally](https://github.com/hmrc/dast-config-manager/blob/main/README.md#running-zap-locally)

The shell script is available to execute ZAP tests. The script proxies the journeys tagged with 'ZapTests' via ZAP.

```bash
./run_local_zap_tests.sh
```

## Scalafmt

**Check all project files are formatted as expected as follows:**

```bash
sbt scalafmtCheckAll scalafmtCheck
```

**Format `*.sbt` and `project/*.scala` files as follows:**

```bash
sbt scalafmtSbt
```

**Format all project files as follows:**

```bash
sbt scalafmtAll
```

[Visit the official Scalafmt documentation to view a complete list of tasks which can be run.](https://scalameta.org/scalafmt/docs/installation.html#task-keys)

## License

**This code is open source software licensed under
the** [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
